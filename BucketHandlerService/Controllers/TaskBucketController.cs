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
public class TaskBucketController : ControllerBase
{
    public StorageClient client { get; set; }
    public string tasksBucket { get; set; }
    public SecretManagerServiceClient secretManager { get; set; }

    public UrlSigner urlSigner { get; set; }

    public TaskBucketController()
    {
        client = StorageClient.Create();
        secretManager = SecretManagerServiceClient.Create();
        tasksBucket = "microservices22tasks_bucket";
        urlSigner = InitializeUrlSigner();
        Console.WriteLine("TaskBucketController initialized.");
    }

    [HttpGet("{taskID}")]
    public ActionResult GetMznAndDzn(string taskID)
    {
        Console.WriteLine($"GetMznAndDzn called for taskid: {taskID}");
        var responseData = new TaskBucketResponse() { TaskID = taskID, MethodAllowed = "GET" };
        if (!client.ListObjects(tasksBucket).Any(x => x.Name.StartsWith($"{taskID}")))
        {
            Console.WriteLine($"No or too many files matched taskID: {taskID}");
            Conflict("No or too many files matched taskID");
        }

        var hasDznFile = client.ListObjects(tasksBucket).Any(x => x.Name == $"{taskID}.dzn");

        //The following lines will mean that problems can no longer be downloaded using the same url after 1 day
        //This means that this endpoint needs to be called before downloading the data everytime
        responseData.ProblemFileUrl = urlSigner.Sign(tasksBucket, $"{taskID}.mzn", TimeSpan.FromDays(1), HttpMethod.Get);
        Console.WriteLine($"ProblemFile has the signed url: {responseData.ProblemFileUrl}");

        if (hasDznFile)
        {
            responseData.DataFileUrl = urlSigner.Sign(tasksBucket, $"{taskID}.dzn", TimeSpan.FromDays(1), HttpMethod.Get);
            Console.WriteLine($"DataFile has the signed url: {responseData.DataFileUrl}");
        }
        var jsonData = JsonSerializer.Serialize(responseData);
        return Ok(jsonData);
    }

    [HttpGet("UploadUrl/{taskID}")]
    public ActionResult PostMzn(string taskID)
    {
        Console.WriteLine($"PostMzn called for taskid: {taskID}");
        var responseData = new TaskBucketResponse() { TaskID = taskID, MethodAllowed = "PUT" };

        var contentHeaders = new Dictionary<string, IEnumerable<string>>
            {
                { "Content-Type", new[] { "application/json" } }
            };

        UrlSigner.Options options = UrlSigner.Options.FromDuration(TimeSpan.FromDays(1));

        UrlSigner.RequestTemplate mznTemplate = UrlSigner.RequestTemplate
            .FromBucket(tasksBucket)
            .WithObjectName($"{taskID}.mzn")
            .WithHttpMethod(HttpMethod.Put)
            .WithContentHeaders(contentHeaders);

        UrlSigner.RequestTemplate dznTemplate = UrlSigner.RequestTemplate
            .FromBucket(tasksBucket)
            .WithObjectName($"{taskID}.dzn")
            .WithHttpMethod(HttpMethod.Put)
            .WithContentHeaders(contentHeaders);

        responseData.ProblemFileUrl = urlSigner.Sign(mznTemplate, options);
        responseData.DataFileUrl = urlSigner.Sign(dznTemplate, options);

        Console.WriteLine($"PostMzn completed and gave the problem url: {responseData.ProblemFileUrl}");
        var jsonData = JsonSerializer.Serialize(responseData).Replace("\\u0026", "&");
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
