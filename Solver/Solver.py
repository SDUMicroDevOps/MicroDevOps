import asyncio
import requests
import json
import os
from minizinc import Instance, Model, Solver, Result, Status
import sys


'''
This class represents a solver

Arguments excpected:
    mzn_file                                - The url of the mzn file
    solver_name                             - The name of the solver to use
    number_processors                       - The number of processors to use
    dzn_file                                - The url of the dzn file


Environment variables expected:
    USERID                                  - The UserID of the user creating this task
    TASKID                                  - A unique ID for this task
    SOLVER_MANAGER_SERVICE                  - The K8 service for DNS lookup 
    SOLUTION_MANAGER_SERVICE                - The K8 service for DNS lookup
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
    
    #This function is here to download the files at some point
    async def get_file(self, path : str):
        await asyncio.sleep(1)
        return path

    async def solve(self):
        solver = Solver.lookup(self.solver_name)

        minizinc_model = Model()

        mzn = await self.get_file(self.mzn_file)
        minizinc_model.add_file(mzn)

        if (self.dzn_file):
            dzn = await self.get_file(self.dzn_file)
            minizinc_model.add_file(dzn)      
          

        to_solve = Instance(solver, minizinc_model)
        
        result = to_solve.solutions(processes=int(self.number_processors))

        timer = 0
        async for i in result:
            if (timer % 100 == 0):
                self.notify_intermediate_solution_found(i)
            result
            timer += 1
            bestResult = i
            print(i.status)

        self.notify_final_solution_found(bestResult)

    def __init__(self, args):
        self.userID = os.getenv("USERID") if os.getenv("USERID") else "testUser"
        
        self.taskID = os.getenv("TASKID") if os.getenv("TASKID") else "testTask"
        self.solver_manager_service = os.getenv("SOLVER_MANAGER_SERVICE") if os.getenv("SOLVER_MANAGER_SERVICE") else ("0.0.0.0")
        self.solution_manager_service = os.getenv("SOLUTION_MANAGER_SERVICE") if os.getenv("SOLUTION_MANAGER_SERVICE") else ("0.0.0.0")
        
        self.mzn_file = args[1]
        self.solver_name = args[2]
        self.number_processors = args[3]
        if (len(args) == 5):
            self.dzn_file = args[4]
        else:
            self.dzn_file = None
        
        self.solver_manager_url = "http://{service}:{port}".format(service=self.solver_manager_service, port=5000)
        self.solution_manager_url = "http://{service}:{port}".format(service=self.solution_manager_service, port=5001)


if __name__ == '__main__':
    solver = SolverInstance(sys.argv)
    asyncio.run(solver.solve())
