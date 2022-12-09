import asyncio
import requests
import json
import time
import os
from minizinc import Instance, Model, Solver, Result
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
                "TaskID": self.taskID,
                "Solution": str(result.solution),
                "UserID" : self.userID,
                "isOptimal" : isOptimal
            })

    def notify_intermediate_solution_found(self, result: Result):
        result_as_json = self.get_result_as_json(result)
        requests.post(self.solution_manager_url + "/SolutionFound", data=result_as_json)

    def notify_final_solution_found(self, result: Result):
        result_as_json = self.get_result_as_json(result, True)
        requests.post(self.solver_manager_url + "/Solution/{taskID}".format(taskID = self.taskID), data=json.dumps({"UserID" : self.userID}))
        requests.post(self.solution_manager_url + "/SolutionFound", data=result_as_json)
    
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

    async def solve(self):
        solver = Solver.lookup(self.solver_name)
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
            bestResult = i

        self.notify_final_solution_found(bestResult)

    def __init__(self, args):
        
        self.solver_manager_service = os.getenv("SOLVER_MANAGER_SERVICE") if os.getenv("SOLVER_MANAGER_SERVICE") else ("0.0.0.0")
        self.solution_manager_service = os.getenv("SOLUTION_MANAGER_SERVICE") if os.getenv("SOLUTION_MANAGER_SERVICE") else ("0.0.0.0")
        self.solver_manager_port = os.getenv("SOLVER_MANAGER_PORT") if os.getenv("SOLVER_MANAGER_PORT") else ("5000")
        self.solution_manager_port = os.getenv("SOLUTION_MANAGER_PORT") if os.getenv("SOLUTION_MANAGER_PORT") else ("5001")
        self.bucket_handler_service = os.getenv("BUCKET_HANDLER_SERVICE") if os.getenv("BUCKET_HANDLER_SERVICE") else ("0.0.0.0")
        self.bucket_handler_port = os.getenv("BUCKET_HANDLER_PORT") if os.getenv("BUCKET_HANDLER_PORT") else ("5165")
        
        self.solver_name = args[1]
        self.number_processors = args[2]
        self.userID = args[3]
        self.taskID = args[4]

        self.solver_manager_url = "http://{service}:{port}".format(service=self.solver_manager_service, port=self.solver_manager_port)
        self.solution_manager_url = "http://{service}:{port}".format(service=self.solution_manager_service, port=self.solution_manager_port)
        self.bucket_handler_url = "http://{service}:{port}".format(service=self.bucket_handler_service, port=self.bucket_handler_port)

if __name__ == '__main__':
    solver = SolverInstance(sys.argv)
    asyncio.run(solver.solve())