package com.oops.backend.Backend.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.util.List;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oops.backend.Backend.models.BucketAddResponse;
import com.oops.backend.Backend.models.BucketResponse;
import com.oops.backend.Backend.models.Solution;
import com.oops.backend.Backend.models.Solver;
import com.oops.backend.Backend.models.SolverBody;
import com.oops.backend.Backend.models.TaskQueue;
import com.oops.backend.Backend.models.User;
import com.oops.backend.Backend.requests.CancelSolverRequest;
import com.oops.backend.Backend.requests.CancelTaskRequest;
import com.oops.backend.Backend.requests.CancelUserTasksRequest;
import com.oops.backend.Backend.requests.SolveRequest;
import com.oops.backend.Backend.requests.SolversToUseBody;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/*
 * Environment variables needed
 * 
 * SOLVER_MANAGER_SERVICE   - Local network address of Solver manager
 * SOLVER_MANAGER_PORT
 * DB_SERVICE       - Address of db service 
 * DB_SERVICE_PORT
 * BUCKER_HANDLER_SERVICE   - Address of bucket handler
 * BUCKET_HANDLER_PORT
 */

@Service
public class BackendService {
    private String solverManagerAddress = "http://" + System.getenv("SOLVER_MANAGER_SERVICE") + ":"
            + System.getenv("SOLVER_MANAGER_PORT");
    private String dbServiceAddress = "http://" + System.getenv("DATABASE_SERVICE") + ":"
            + System.getenv("DATABASE_PORT");
    private String bucketHandlerAddress = "http://" + System.getenv("BUCKET_HANDLER_SERVICE") + ":"
            + System.getenv("BUCKET_HANDLER_PORT");

    private RestTemplate restTemplate = new RestTemplate();

    ObjectMapper objectMapper = new ObjectMapper();

    public BackendService() {
    }

    public List<User> getUsers() throws IOException, InterruptedException {
        String url = dbServiceAddress + "/users";
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(
                        url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        String responseBody = response.body();

        List<User> users = gson.fromJson(responseBody, new TypeToken<List<User>>() {
        }.getType());

        return users;
    }

    public void postSolversToSolverManager(SolveRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        var json_string = gson.toJson(request);
        var sm_request = HttpRequest.newBuilder(
                URI.create(solverManagerAddress + "/new"))
                .POST(BodyPublishers.ofString(json_string))
                .header("Content-Type", "application/json")
                .build();

        System.out.println("Contacting the SolverManager at adress: " + sm_request.uri().toString());
        var resp = client.send(sm_request, BodyHandlers.ofString()); // TODO, maybe make some sort of error handling
                                                                     // here
        System.out.println("Response from SolverManager: " + resp.statusCode());
    }

    public String getAllLegalSolvers() throws JsonProcessingException {
        String url = dbServiceAddress + "/solvers";
        Solver[] solvers = restTemplate.getForObject(url, Solver[].class);
        String jsonData = objectMapper.writeValueAsString(solvers);

        return jsonData;
    }

    public byte[] getMznData(String ProblemID) throws IOException {
        String url = bucketHandlerAddress + "/TaskBucket/" + ProblemID;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        BucketResponse bucketResponse = objectMapper.readValue(response.getBody(), BucketResponse.class);

        // Set the URL of the file you want to download
        URL downloadUrl = new URL(bucketResponse.getProblemFileUrl());

        // Open a connection to the URL
        URLConnection connection = downloadUrl.openConnection();

        // Get an input stream for reading from the URL
        InputStream in = connection.getInputStream();

        // Read the contents of the file into a byte array
        BufferedInputStream input = new BufferedInputStream(in);
        byte[] fileContents = StreamUtils.copyToByteArray(input);

        // Close the input stream
        in.close();

        return fileContents;
    }

    public String addMznData(String UserID, MultipartFile mznFile) throws IOException, InterruptedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String fileExtension = StringUtils.cleanPath(mznFile.getOriginalFilename());
        String fileName = UserID + timestamp.getTime() + fileExtension;
        String problemID = fileName.replace(fileExtension, "");

        String url = bucketHandlerAddress + "/TaskBucket/uploadurl/" + problemID;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        BucketResponse bucketResponse = objectMapper.readValue(response.getBody(), BucketResponse.class);

        byte[] fileData = mznFile.getBytes();
        OutputStream out = new FileOutputStream(new File(fileName));
        out.write(fileData);
        out.close();

        if (bucketResponse != null) {

            String uploadUrl = bucketResponse.getDataFileUrl();

            ProcessBuilder pb = new ProcessBuilder(
                    "curl",
                    "-X", "PUT",
                    "-H", "Content-Type: application/json",
                    "--upload-file", fileName,
                    uploadUrl);

            Process p = pb.start();
            p.waitFor();
        }

        File file = new File(fileName);
        file.delete();

        return problemID;
    }

    private String removeQuotesAndUnescape(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("^\"|\"$", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }

    public String addDznData(String UserID, MultipartFile dznFile) throws IOException, InterruptedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String fileExtension = StringUtils.cleanPath(dznFile.getOriginalFilename());
        String fileName = UserID + timestamp.getTime() + fileExtension;

        String problemID = fileName.replace(fileExtension, "");
        String url = bucketHandlerAddress + "/TaskBucket/uploadurl/" + problemID;

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(
                        url))
                .GET()
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request,
                BodyHandlers.ofString());
        BucketAddResponse bucketResponse = gson.fromJson(this.removeQuotesAndUnescape(response.body()),
                BucketAddResponse.class);

        byte[] fileData = dznFile.getBytes();
        OutputStream out = new FileOutputStream(new File(fileName));
        out.write(fileData);
        out.close();

        String uploadUrl = bucketResponse.getDataFileUrl();

        if (bucketResponse != null) {
            ProcessBuilder pb = new ProcessBuilder(
                    "curl",
                    "-X", "PUT",
                    "-H", "Content-Type: application/json",
                    "--upload-file", fileName,
                    uploadUrl);

            Process p = pb.start();
            p.waitFor();
        }

        File file = new File(fileName);
        file.delete();

        return problemID;
    }

    public byte[] getDznData(String dataID) throws IOException {
        String url = bucketHandlerAddress + "/TaskBucket/" + dataID;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        BucketResponse bucketResponse = objectMapper.readValue(response.getBody(), BucketResponse.class);

        // Set the URL of the file you want to download
        URL downloadUrl = new URL(bucketResponse.getDataFileUrl());

        // Open a connection to the URL
        URLConnection connection = downloadUrl.openConnection();

        // Get an input stream for reading from the URL
        InputStream in = connection.getInputStream();

        // Read the contents of the file into a byte array
        BufferedInputStream input = new BufferedInputStream(in);
        byte[] fileContents = StreamUtils.copyToByteArray(input);

        // Close the input stream
        in.close();

        return fileContents;
    }

    public String getAllUserSolvers(String UserID) throws JsonProcessingException {
        String url = solverManagerAddress + "/user/" + UserID + "/solvers";
        SolverBody[] solvers = restTemplate.getForObject(url, SolverBody[].class);
        String jsonData = objectMapper.writeValueAsString(solvers);
        return jsonData;
    }

    public String getAllSoversForProlem(String ProblemID) throws JsonProcessingException {
        String url = solverManagerAddress + "/problem/" + ProblemID + "/solvers";
        SolverBody[] solvers = restTemplate.getForObject(url, SolverBody[].class);
        String jsonData = objectMapper.writeValueAsString(solvers);
        return jsonData;
    }

    public String getResultOfTask(String ProblemID) {
        String url = dbServiceAddress + "/solutions/" + ProblemID;
        Solution solution = restTemplate.getForObject(url, Solution.class);
        if (solution != null) {
            if (solution.getIsOptimal()) {
                return solution.getContent();
            } else
                return null;
        }
        return null;
    }

    public void cancelTasksForUser(CancelUserTasksRequest cancelUserTaskRequest) {
        String url = solverManagerAddress + "/cancel/user";
        restTemplate.postForEntity(url, cancelUserTaskRequest,
                CancelUserTasksRequest.class);

    }

    public String cancelTask(String TaskID, CancelTaskRequest cancelTaskRequest) {
        String url = solverManagerAddress + "/cancel/task/" + TaskID;
        restTemplate.postForEntity(url, cancelTaskRequest,
                CancelTaskRequest.class);
        return TaskID;
    }

    public String cancelSolver(String solverName, CancelSolverRequest cancelSolverRequest) {
        String url = solverManagerAddress + "/cancel/user";
        restTemplate.postForEntity(url, cancelSolverRequest,
                CancelTaskRequest.class);
        return solverName;
    }

}
