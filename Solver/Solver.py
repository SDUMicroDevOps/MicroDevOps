import asyncio
from datetime import date
import requests
import json
import time
import os
from minizinc import Instance, Model, Solver, Result
import google.cloud.logging
import logging
import sys

'''
This class represents a solver

Arguments excpected:
    solver_name                             - The name of the solver to use
    number_processors                       - The number of processors to use
    userID                                  - The UserID of the user creating this task
    taskID                                  - The unique ID of this task

Environment variables expected:
    SOLVER_MANAGER_SERVICE                  - The K8 service for DNS lookup 
    SOLVER_MANAGER_PORT                     - The port for the solver manager
    SOLUTION_MANAGER_SERVICE                - The K8 service for DNS lookup
    SOLUTION_MANAGER_PORT                   - The port for the solution manager
    BUCKET_HANDLER_SERVICE                  - The k8 service for DNS lookup
    BUCKET_HANDLER_PORT                     - The port for the bucket handler service
'''
class SolverInstance:

    def get_result_as_json(self, result: Result, isOptimal = False):
        return json.dumps(
            {
                "taskId": self.taskID,
                "user" : self.userID,
                "content": str(result.solution),
                "date" : f"{date.today().year}-{date.today().month}-{date.today().day}",
                "isOptimal" : isOptimal
            })

    def notify_intermediate_solution_found(self, result: Result):
        try:
            result_as_json = self.get_result_as_json(result)
            requests.post(self.solution_manager_url + "/solutions", json=result_as_json)
        except:
            self.logger(f"{self.solver_manager_url}/debug", data="Connection to the solution manager was not possible")
            
    def notify_optimal_solution(self, result: Result):
        try:
            result_as_json = self.get_result_as_json(result, True)
            requests.post(self.solver_manager_url + f"/solution/{self.taskID}", json=json.dumps({"userID" : self.userID}))
            requests.post(self.solution_manager_url + "/solutions", json=result_as_json)
        except:
            self.logger(f"{self.solver_manager_url}/debug", data="Connection to the solver manager was not possible")

        
    #Downloads and saves the files corresponding to this taskID
    #Returns True if a DZN file was found, otherwise returns false
    async def get_files(self):
        resp = requests.get(self.bucket_handler_url + "/TaskBucket/{taskID}".format(taskID = self.taskID))
        urls = resp.json()
        problem_file_response = requests.get(urls["ProblemFileUrl"])
        with open("mzn.mzn", "wb") as f:
            f.write(problem_file_response.content)
        try:
            data_file_response = requests.get(urls["DataFileUrl"])
            with open("dzn.dzn", "wb") as f:
                f.write(data_file_response.content)
            return True
        except:
            return False

    async def download_solver(self):
        resp = requests.get(self.bucket_handler_url + f"/SolverBucket/{self.solver_name}")
        urls = json.loads(resp.content)
        solver = requests.get(urls["SolverURL"])
        config = requests.get(urls["ConfigURL"])
        
        if not os.path.exists(r"/Downloads"):
            os.makedirs(r"/Downloads")
        with open(f"/Downloads/{self.solver_name}", "wb") as f:
            f.write(solver.content)
        with open(f"/app/MiniZincIDE-2.6.4-bundle-linux-x86_64/share/minizinc/solvers/{self.solver_name}.msc", "wb") as f:
            f.write(config.content)

        return


    async def solve(self):
        try:
            solver = Solver.lookup(self.solver_name)
        except:
            await self.download_solver()
            solver = Solver.lookup(self.solver_name, refresh=True)

        minizinc_model = Model()

        has_dzn_file = await self.get_files()
        minizinc_model.add_file("mzn.mzn")

        if (has_dzn_file):
            minizinc_model.add_file("dzn.dzn")     

        to_solve = Instance(solver, minizinc_model)
        
        result = to_solve.solutions(processes=int(self.number_processors))

        zero_time = time.time()             #Returns current time in seconds
        next_update_interval = zero_time    #Always send first satisfied solution
        async for i in result:
            timer = time.time() - zero_time
            if ( (zero_time + timer) >= next_update_interval):    #Post the first solution, and thereafter one every 20 seconds
                self.notify_intermediate_solution_found(i)
                next_update_interval += 20                        #Wait 20 seconds to send next message
            timer += 1
            if (i.solution != None):
                bestResult = i

        self.notify_optimal_solution(bestResult)

    def __init__(self, args, debug):
        if debug:
            self.logger = requests.post
        else:
            self.logger = print_log
        

        # self.solver_manager_service = os.getenv("SOLVER_MANAGER_SERVICE", "0.0.0.0")
        self.solver_manager_service = "solver-manager-service"
        self.solution_manager_service = os.getenv("DATABASE_SERVICE", "0.0.0.0")
        # self.solver_manager_port = os.getenv("SOLVER_MANAGER_PORT", "5000")
        self.solver_manager_port = 80
        self.solution_manager_port = os.getenv("DATABASE_PORT", "5001")
        self.bucket_handler_service = os.getenv("BUCKET_HANDLER_SERVICE", "0.0.0.0")
        self.bucket_handler_port = os.getenv("BUCKET_HANDLER_PORT", "5165")
        
        self.solver_name = args[1]
        self.number_processors = args[2]
        self.userID = args[3]
        self.taskID = args[4]

        self.solver_manager_url = f"http://{self.solver_manager_service}:{self.solver_manager_port}"
        self.solution_manager_url = f"http://{self.solution_manager_service}:{self.solution_manager_port}"
        self.bucket_handler_url = f"http://{self.bucket_handler_service}:{self.bucket_handler_port}"

        print(f"{self.solver_manager_url}/debug", data=f"A new solver has been created")
        self.logger(f"{self.solver_manager_url}/debug", data=f"A new solver has been created")

def print_log(url, data):
    print(f"{data} was sent to {url}")

if __name__ == "__main__":
    solver = SolverInstance(sys.argv, False)
    asyncio.run(solver.solve())
