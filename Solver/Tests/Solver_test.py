import os
import unittest

from Solver import SolverInstance

class TestSolverMethods(unittest.TestCase):

    def test_solver_reads_env_variables(self):
        #Arrange
        os.environ["SOLVER_MANAGER_SERVICE"] = "TestService"

        #Act
        solver = self.get_new_solver()

        #Assert
        solver.solver_manager_service = "TestService"

    def get_new_solver(self) -> SolverInstance:
        return SolverInstance(["Solver.py", "chuffed", "1", "uid", "tid"])

if __name__ == '__main__':
    unittest.main()
