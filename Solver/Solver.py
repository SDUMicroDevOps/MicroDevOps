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

    userID = ""
    taskID = ""
    has_dzn_file = False
    solver_name = ""
    #mzn_file = ""
    mzn_file = "Examples/shortest.mzn"
    # number_processors = ""
    #dzn_file = ""
    dzn_file = "Examples/shortest.dzn"

    def notify_intermediate_solution_found(self, result: Result):
        return
        result_as_json = json.dumps(
            {
                "TaskID":"{id}".format(id = self.taskID),
                "Solution":"{result}".format(result = result.solution),
                "UserID" : "{UserID}".format(UserID = self.userID)
            })
        self.solution_manager_connection.request(method="POST", url="/SolutionFound", body= result_as_json)
        pass

    def notify_final_solution_found(self, result: AsyncIterator[Result]):
        pass

    
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
        
        result = to_solve.solutions()

        timer = 0

        async for i in result:
            if (timer % 100 == 0):
                self.notify_intermediate_solution_found(i)
            print(i)
            result
            timer += 1

        self.notify_final_solution_found(result)

    def __init__(self, args):
        self.userID = args[1]
        self.taskID = args[2]
        self.has_dzn_file = args[3].upper() == "TRUE"
        self.solver_name = args[4]
        #mzn_file = args[5]
        self.mzn_file = "Examples/shortest.mzn"
        # number_processors = args[6]
        #dzn_file = ""
        self.dzn_file = "Examples/shortest.dzn"
        
        self.solver_manager_connection = hc.HTTPConnection(host="http://solver_manager_servie", port=5000)
        self.solution_manager_connection = hc.HTTPConnection(host="http://solution_manager_connnection", port=5000)

        asyncio.run(self.main())


            


if __name__ == '__main__':

    solver = SolverInstance(sys.argv)