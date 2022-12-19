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
        self.assertEqual(json, '{"taskId": "tid", "user": "uid", "content": "123", "data": "2022-12-19", "isOptimal": false}')

    def test_get_result_as_json_isOptimal_is_assigned(self):
        #Arrange
        solver = self.get_new_solver()
        result = Result(statistics=None, solution="123", status=Status.OPTIMAL_SOLUTION)

        #Act
        json = solver.get_result_as_json(result, True)

        #Assert
        self.assertEqual(json, '{"taskId": "tid", "user": "uid", "content": "123", "data": "2022-12-19", "isOptimal": true}')

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
