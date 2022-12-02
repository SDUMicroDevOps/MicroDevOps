import os
import unittest

from minizinc import Result, Status
from Solver import SolverInstance

class TestSolverMethods(unittest.TestCase):
    
    def test_get_result_as_json_returns_json_file(self):
        #Arrange
        solver = self.get_new_solver()
        result = Result(statistics=None, solution="123", status=Status.OPTIMAL_SOLUTION)

        #Act
        json = solver.get_result_as_json(result)

        #Assert
        self.assertIsInstance(json, str)

    def test_get_result_as_json_contains_correcct_info(self):
        #Arrange
        solver = self.get_new_solver()
        result = Result(statistics=None, solution="123", status=Status.OPTIMAL_SOLUTION)

        #Act
        json = solver.get_result_as_json(result)

        #Assert
        self.assertEqual(json, '{"TaskID": "testTask", "Solution": "123", "UserID": "testUser", "isOptimal": false}')

    def test_get_result_as_json_isOptimal_is_assigned(self):
        #Arrange
        solver = self.get_new_solver()
        result = Result(statistics=None, solution="123", status=Status.OPTIMAL_SOLUTION)

        #Act
        json = solver.get_result_as_json(result, True)

        #Assert
        self.assertEqual(json, '{"TaskID": "testTask", "Solution": "123", "UserID": "testUser", "isOptimal": true}')

    def test_solver_reads_env_variables(self):
        #Arrange
        userID = "xX-M4STER_US3R_420_69-Xx"
        os.environ["USERID"] = userID

        #Act
        solver = self.get_new_solver()

        #Assert
        self.assertEqual(solver.userID, userID)
        self.assertNotEqual(solver.userID, "testUser")
        self.assertEqual(solver.taskID, "testTask")


    def get_new_solver(self) -> SolverInstance:
        return SolverInstance(["Solver.py", "../TestData/aust.mzn", "chuffed", "1"])

if __name__ == '__main__':
    unittest.main()