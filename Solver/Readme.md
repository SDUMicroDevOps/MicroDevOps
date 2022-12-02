Contains files associated with the solver instance to be used by the Solver Manager from a job.

The solver takes 3 arguments, plus one optional 4th argument containing an eventual dnz file.
In addition to the arguments, the solver is configured using environemnt variables, which are then read at runtime.

To run the Unittest from the "solver" directory, run the command 'python3 -m unittest Tests/Solver_test.py'

Since the solver sends HTTP updates to the SolverManager and SolutionManager when running, the 'Tests' directory also contains two flask servers which can be
run to emulate the SolutionManager and SolverManager respectively. To run the solver with a test problem from python, these need to be running.

The image produced from the dockerfile can for the same reason not be run without the SolverManager and SolutionManager running in the cluster