import asyncio
import http.client as hc
import json
from typing import AsyncIterator, List
from minizinc import Instance, Model, Solver, Result
import sys

'''
This class represents a solver
Arguments expected:
    userID: string                          - The UserID of the user creating this task
    taskID: string                          - A unique ID for this task
    has_dzn_file: True || False             - Describes whether a DZN file will be provided
    solver_name: 'chuffed' || 'geode'       - The name of the solver to use
    mzn_file: string                        - The url of the mzn_file
    dzn_file: string                        - If hasDznFile was true, the url of the dzn_file
    number_processors : int                 - The number of processors to use
'''

class SolverInstance:

    userID = "testUser"
    taskID = "testTask"
    has_dzn_file = True
    solver_name = "chuffed"
    mzn_file = "Test/shortest.mzn"
    number_processors = "2"
    dzn_file = "Test/shortest.dzn"

    def get_result_as_json(self, result: Result):
        return json.dumps(
            {
                "TaskID": self.taskID,
                "Solution": str(result.solution),
                "UserID" : self.userID
            })

    def notify_intermediate_solution_found(self, result: Result):
        result_as_json = self.get_result_as_json(result)
        self.solution_manager_connection.request(method="POST", url="/SolutionFound", body= result_as_json)
        

    def notify_final_solution_found(self, result: Result):
        result_as_json = self.get_result_as_json(result)
        self.solver_manager_connection.request(method="POST", url="/Solution/{taskID}".format(taskID = self.taskID), body= json.dumps({"UserID" : self.userID}))
        self.solution_manager_connection.request(method="POST", url="/SolutionFound", body= result_as_json)
    
    #This function is here to download the files at some point
    async def get_file(self, path : str):
        await asyncio.sleep(1)
        return path

    async def main(self):
        solver = Solver.lookup(self.solver_name)

        minizinc_model = Model()
        mzn = await self.get_file(self.mzn_file)
        minizinc_model.add_file(mzn)

        if (self.has_dzn_file):
            #self.dzn_file = sys.argv[7]
            dzn = await self.get_file(self.dzn_file)
            minizinc_model.add_file(dzn)
                
        to_solve = Instance(solver, minizinc_model)
        
        result = to_solve.solutions(processes=int(self.number_processors))

        timer = 0
        async for i in result:
            if (timer % 100 == 0):
                self.notify_intermediate_solution_found(i)
            print(i)
            result
            timer += 1
            bestResult = i

        self.notify_final_solution_found(bestResult)

    def __init__(self, args):
        if (len(args) > 1):
            self.userID = args[1]
            self.taskID = args[2]
            self.has_dzn_file = args[3].upper() == "TRUE"
            self.solver_name = args[4]
            self.mzn_file = args[5]
            self.number_processors = args[6]
            self.dzn_file = args[7]
        
        self.solver_manager_connection = hc.HTTPConnection(host="http://solver_manager_service", port=5000)
        self.solution_manager_connection = hc.HTTPConnection(host="http://solution_manager_service", port=5000)

        asyncio.run(self.main())

if __name__ == '__main__':
    solver = SolverInstance(sys.argv)