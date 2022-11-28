import asyncio
from time import sleep
from minizinc import Instance, Model, Solver, Status, Result
import sys
import requests

'''
This class represents a solver
Arguments expected:
    id: string                              - A unique ID for this task
    has_dzn_file: True || False             - Describes whether a DZN file will be provided
    solver_name: 'chuffed' || 'geode'       - The name of the solver to use
    mznFile: string                         - The data to process
    dznFile: string                         - If hasDznFile was true, the contents of a DZN file

'''

def check_args():
    if (False):
        raise RuntimeError("Arguments wrong", "Error")

async def main():
    check_args()
    id = sys.argv[1]
    has_dzn_file = sys.argv[2].capitalize == "FALSE"
    solver_name = sys.argv[3]
    # mzn_file_contents = sys.argv[4]
    mzn_file = "Examples/shortest.mzn" 

    if (has_dzn_file):
        with open("dznFile.dzn", 'rw') as f:
            f.write(sys.argv[5])
    
    solver = Solver.lookup(solver_name)

    minizinc_model = Model()
    minizinc_model.add_file(mzn_file)
    minizinc_model.add_file("Examples/shortest.dzn")

    if (has_dzn_file):
        minizinc_model.add_file("dznFile.dzn")
            
    to_solve = Instance(solver, minizinc_model)
    
    result = to_solve.solutions()
    async for i in result:
        print(i)

if __name__ == '__main__':
    asyncio.run(main())    