using System.Text.Json;
using Google.Cloud.Storage.V1;
using Microsoft.AspNetCore.Mvc;


namespace BucketHandlerService.Controllers;

/*
*   Environment variables relied upon:
*       PROJECT_ID:                         The id of the google cloud project
*       GOOGLE_APPLICATION_CREDENTIALS:     The path to a application_default_credentials.json file with access to the project
*       GOOGLE_SERVICE_ACCOUNT_KEY:         The path to the default_service_account_key.json file. Is a secret. Shhhhhhh.
*/
[ApiController]
[Route("[controller]")]
public class TaskBucketController : ControllerBase
{
    public string? projectID = Environment.GetEnvironmentVariable("PROJECT_ID");
    public string? credentialFilePath = Environment.GetEnvironmentVariable("GOOGLE_SERVICE_ACCOUNT_KEY");
    public StorageClient client = StorageClient.Create();
    public string tasksBucket = "microservices22tasks_bucket";

    [HttpGet("{taskID}")]
    public string GetMznAndDzn(string taskID)
    {
        var urlSigner = UrlSigner.FromServiceAccountPath(credentialFilePath);   //TODO: Use K8 secret for this
        var responseData = new BucketResponse() { TaskID = taskID, MethodAllowed = "GET" };

        if (!client.ListObjects(tasksBucket).Any(x => x.Name.StartsWith($"{taskID}")))
        {
            throw new ArgumentException("No or too many files matched taskID");
        }

        var hasDznFile = client.ListObjects(tasksBucket).Any(x => x.Name == $"{taskID}.dzn");

        //The following lines will mean that problems can no longer be downloaded using the same url after 1 day
        //This means that this endpoint needs to be called before downloading the data everytime
        responseData.ProblemFileUrl = urlSigner.Sign(tasksBucket, $"{taskID}.mzn", TimeSpan.FromDays(1), HttpMethod.Get);
        if (hasDznFile)
        {
            responseData.DataFileUrl = urlSigner.Sign(tasksBucket, $"{taskID}.dzn", TimeSpan.FromDays(1), HttpMethod.Get);
        }
        var jsonData = JsonSerializer.Serialize(responseData);
        return jsonData;
    }

    [HttpGet("uploadurl/{taskID}")]
    public string PostMzn(string taskID)
    {
        var urlSigner = UrlSigner.FromServiceAccountPath(credentialFilePath);   //TODO: Use K8 secret for this
        var responseData = new BucketResponse() { TaskID = taskID, MethodAllowed = "PUT" };

        var contentHeaders = new Dictionary<string, IEnumerable<string>>
        {
            { "Content-Type", new[] { "text/plain" } }
        };

        UrlSigner.Options options = UrlSigner.Options.FromDuration(TimeSpan.FromDays(1));

        UrlSigner.RequestTemplate mznTemplate = UrlSigner.RequestTemplate
            .FromBucket(tasksBucket)
            .WithObjectName($"{taskID}.mzn")
            .WithHttpMethod(HttpMethod.Put)
            .WithContentHeaders(contentHeaders);

        UrlSigner.RequestTemplate dznTemplate = UrlSigner.RequestTemplate
            .FromBucket(tasksBucket)
            .WithObjectName($"{taskID}.mzn")
            .WithHttpMethod(HttpMethod.Post)
            .WithContentHeaders(contentHeaders);

        responseData.ProblemFileUrl = urlSigner.Sign(mznTemplate, options);
        Console.WriteLine(responseData.ProblemFileUrl);
        responseData.DataFileUrl = urlSigner.Sign(dznTemplate, options);

        var jsonData = JsonSerializer.Serialize(responseData);
        return jsonData;
    }
}
