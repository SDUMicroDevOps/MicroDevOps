using Microsoft.AspNetCore.Mvc;
using ChuffedSolver.Services;
using System.Text.Json;
using System.IO;

namespace ChuffedSolver.Controllers;

[ApiController]
[Route("[controller]")]
public class SolverController : ControllerBase
{
    List<(string userID, SolverService service, CancellationTokenSource cancellationToken)> ongoingSolvers;
    HttpClient client = new HttpClient();

    /// <summary>
    /// Starts a new solver of type {solver}
    /// </summary>
    /// <param name="solver"></param>
    /// <returns></returns>
    [HttpPost]
    [Route("Solve/{solver}")]
    public string Solve(string uid, string mznFile, string solver)
    {
        if (solver == "chuffed") //TODO: replace this with LegalSolver.Any(x => x == solver);
        {
            System.IO.File.Create($"{uid}_toSolve.mzn");
            using StreamWriter file = new("toSolve.mzn", append: false);
            file.WriteLine(mznFile);
            StartCalculation(uid, solver);
            return "Request is being correctly processed";
        }
        else
        {
            return $"Invalid solver request: {solver}";
        }
    }

    private void StartCalculation(string uid, string solver)
    {
        var cancelToken = new CancellationTokenSource();
        var solverToStart = new SolverService(this, uid, cancelToken.Token);
        ongoingSolvers.Append((uid, solverToStart, cancelToken));
        solverToStart.StartCalculation(solver);     //Intended behaviour, don't wait for call
    }

    /// <summary>
    /// Stops the calculation for any solver from uid.
    /// </summary>
    [HttpPost]
    [Route("Stop")]
    public void StopCalculation(string uid)
    {
        var solverToStop = ongoingSolvers.Where(x => x.userID == uid).First();
        solverToStop.cancellationToken.Cancel();
    }

    [ApiExplorerSettings(IgnoreApi = true)]
    public async Task ComputationCompleted(bool solverCompleted, string uid)
    {
        if (solverCompleted)
        {
            var solution = System.IO.File.ReadAllLines($"{uid}_output.txt");
            var jsonResult = JsonSerializer.Serialize((uid, solution));
            await client.PostAsJsonAsync("http://localhost:5000/solverResult", jsonResult);
        }
    }
}
