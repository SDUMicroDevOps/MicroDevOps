using System.Net;
using System.Text.Json;
using Google.Cloud.SecretManager.V1;

using Google.Cloud.Storage.V1;
using Microsoft.AspNetCore.Mvc;


namespace BucketHandlerService.Controllers;

/*
*   Environment variables relied upon:
*       GOOGLE_APPLICATION_CREDENTIALS:     The path to a application_default_credentials.json file with access to the project. Is set in GKE
*/

[ApiController]
[Route("[controller]")]
public class SolverBucketController : ControllerBase
{
    public StorageClient client;
    public string solverBucket;
    public SecretManagerServiceClient secretManager;

    public UrlSigner urlSigner;

    public SolverBucketController()
    {
        client = StorageClient.Create();
        secretManager = SecretManagerServiceClient.Create();
        solverBucket = "microservices22solvers_bucket";
        urlSigner = InitializeUrlSigner();
        Console.WriteLine("SolverBucketController initialized.");
    }

    [HttpGet("{solverName}")]
    public ActionResult GetSolver(string solverName)
    {
        Console.WriteLine($"GetSolver called for solvername: {solverName}");

        var responseData = new SolverBucketResponse() { SolverName = solverName, ConfigData = "Downloadable from ConfigURL", MethodAllowed = "GET" };

        if (!client.ListObjects(solverBucket).Any(x => x.Name.StartsWith($"{solverName}")))
        {
            Console.WriteLine($"No solvers with the name '{solverName}' found on bucket storage");
            Conflict($"No solvers with the name '{solverName}' found on bucket storage");
        }

        //The following lines will mean that problems can no longer be downloaded using the same url after 1 day
        //This means that this endpoint needs to be called before downloading the data everytime
        responseData.ConfigURL = urlSigner.Sign(solverBucket, $"{solverName}.msc", TimeSpan.FromDays(1), HttpMethod.Get);
        responseData.SolverURL = urlSigner.Sign(solverBucket, $"{solverName}", TimeSpan.FromDays(1), HttpMethod.Get);

        var jsonData = JsonSerializer.Serialize(responseData).Replace("\\u0026", "&");
        Console.WriteLine($"Solver returned as URL: {responseData.SolverURL}");
        return Ok(jsonData);
    }

    [HttpPost]
    public ActionResult PostSolver([FromBody] SolverRequest request)
    {
        Console.WriteLine($"PostSolver called with solvername: {request.SolverName}");
        if (client.ListObjects(solverBucket).Any(x => x.Name.StartsWith($"{request.SolverName}")))
        {
            Console.WriteLine($"Solver already exists!");
            return Conflict($"A solver with the name '{request.SolverName}' is already present");
        }

        var configContents = new ConfigResponse()
        {
            ID = $"id.{request.SolverName}",
            Name = request.SolverName,
            Executable = $"/Downloads/{request.SolverName}",
            Version = request.Version,
            SupportsMzn = request.SupportsMzn.ToUpper() == "TRUE",
            SupportsFzn = request.SupportsFzn.ToUpper() == "TRUE"
        };

        var responseData = new SolverBucketResponse()
        {
            SolverName = request.SolverName,
            ConfigData = JsonSerializer.Serialize(configContents),
            MethodAllowed = "PUT"
        };

        var contentHeaders = new Dictionary<string, IEnumerable<string>>
            {
                { "Content-Type", new[] { "application/json" } }
            };

        UrlSigner.Options options = UrlSigner.Options.FromDuration(TimeSpan.FromDays(1));

        UrlSigner.RequestTemplate solverTemplate = UrlSigner.RequestTemplate
            .FromBucket(solverBucket)
            .WithObjectName(request.SolverName)
            .WithHttpMethod(HttpMethod.Put)
            .WithContentHeaders(contentHeaders);

        UrlSigner.RequestTemplate configTemplate = UrlSigner.RequestTemplate
            .FromBucket(solverBucket)
            .WithObjectName($"{request.SolverName}.msc")
            .WithHttpMethod(HttpMethod.Put)
            .WithContentHeaders(contentHeaders);

        responseData.SolverURL = urlSigner.Sign(solverTemplate, options);
        responseData.ConfigURL = urlSigner.Sign(configTemplate, options);

        var jsonData = JsonSerializer.Serialize(responseData).Replace("\\u0026", "&").Replace("\\u0022", "\"");
        Console.WriteLine($"SolverURLs correctly build: {responseData.SolverURL} && {responseData.ConfigURL}");
        return Ok(jsonData);
    }

    private UrlSigner InitializeUrlSigner()
    {
        UrlSigner signer;
        using (var stream = GetSecretCredentials())
        {
            signer = UrlSigner.FromServiceAccountData(stream);
        }
        return signer;
    }

    private Stream GetSecretCredentials()
    {
        var secretResourceName = "projects/859134286483/secrets/default_service_account_key/versions/1";
        var secret = secretManager.AccessSecretVersion(secretResourceName).Payload.Data.ToStringUtf8();
        return GenerateStreamFromString(secret);
    }

    private static Stream GenerateStreamFromString(string s)
    {
        var stream = new MemoryStream();
        var writer = new StreamWriter(stream);
        writer.Write(s);
        writer.Flush();
        stream.Position = 0;
        return stream;
    }
}
