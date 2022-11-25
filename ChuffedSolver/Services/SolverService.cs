
using System.Diagnostics;
using ChuffedSolver.Controllers;

namespace ChuffedSolver.Services
{
    public class SolverService
    {

        SolverController initiator;
        string userID;
        CancellationToken token;
        HttpClient client = new HttpClient();

        public SolverService(SolverController initiator, string userID, CancellationToken token)
        {
            this.initiator = initiator;
            this.userID = userID;
            this.token = token;
        }

        public async Task StartCalculation(string solverName)
        {
            var fileName = $"{userID}_toSolve.mzn";
            var solverProcess = new Process();
            var solverInfo = new ProcessStartInfo() { FileName = "minizinc", Arguments = $"--solver {solverName} {userID}_toSolve.mzn --output-raw {userID}_output.txt" };

            Process.Start(solverInfo);
            while (!solverProcess.HasExited)
            {
                if (token.IsCancellationRequested)
                {
                    solverProcess.Kill();
                    break;
                }
                Thread.Sleep(100);
            }
            if (File.Exists($"{userID}_output.txt"))
            {
                initiator.ComputationCompleted(true, userID);
            }
            else
            {
                initiator.ComputationCompleted(false, userID);
            }

        }
    }
}