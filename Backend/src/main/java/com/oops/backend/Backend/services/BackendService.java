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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oops.backend.Backend.models.BucketAddResponse;
import com.oops.backend.Backend.models.BucketResponse;
import com.oops.backend.Backend.models.Solution;
import com.oops.backend.Backend.models.Solver;
import com.oops.backend.Backend.models.SolverBody;
import com.oops.backend.Backend.models.User;
import com.oops.backend.Backend.requests.CancelSolverRequest;
import com.oops.backend.Backend.requests.CancelTaskRequest;
import com.oops.backend.Backend.requests.CancelUserTasksRequest;
import com.oops.backend.Backend.requests.SolveRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

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
    private String authServiceAddress = "http://" + System.getenv("AUTH_SERVICE") + ":"
            + System.getenv("AUTH_PORT");

    private RestTemplate restTemplate = new RestTemplate();

    ObjectMapper objectMapper = new ObjectMapper();

    public BackendService() {
    }

    public String authenticate(String token) throws JsonMappingException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(authServiceAddress + "/verify", HttpMethod.GET, entity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            final ObjectNode node = new ObjectMapper().readValue(response.getBody(), ObjectNode.class);

            if (node.has("Type")) {
                return node.get("Type").toString();
            } else
                return "No user type found";
        } else
            return "Authorization error!";

    }

    public List<User> getUsers(String token) throws IOException, InterruptedException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
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
        } else {
            System.out.println("Authorization error");
            return null;
        }
    }

    public void postSolversToSolverManager(SolveRequest request, String token)
            throws IOException, InterruptedException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
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
        } else
            System.out.println("Authorization error!");
    }

    public String getAllLegalSolvers(String token) throws JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = dbServiceAddress + "/solvers";
            Solver[] solvers = restTemplate.getForObject(url, Solver[].class);
            String jsonData = objectMapper.writeValueAsString(solvers);

            return jsonData;
        } else
            return "Authorization error";
    }

    public byte[] getMznData(String ProblemID, String token) throws IOException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
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
        } else {
            System.out.println("Authorization error");
            return null;
        }
    }

    public String addMznData(String UserID, MultipartFile mznFile, String token, Timestamp timestamp)
            throws IOException, InterruptedException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {

            String fileName = UserID + timestamp.getTime() + ".mzn";
            String problemID = fileName.replace(".mzn", "");

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
        } else
            return "Authorization error";
    }

    private String removeQuotesAndUnescape(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("^\"|\"$", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }

    public String addDznData(String UserID, MultipartFile dznFile, String token, Timestamp timestamp)
            throws IOException, InterruptedException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {

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
        } else {
            return "Authorization error!";
        }
    }

    public byte[] getDznData(String dataID, String token) throws IOException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
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
        } else {
            System.out.println("Authorization error!");
            return null;
        }
    }

    public String getAllUserSolvers(String UserID, String token) throws JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = solverManagerAddress + "/user/" + UserID + "/solvers";
            SolverBody[] solvers = restTemplate.getForObject(url, SolverBody[].class);
            String jsonData = objectMapper.writeValueAsString(solvers);
            return jsonData;
        } else
            return "Authorization error";
    }

    public String getAllSoversForProlem(String ProblemID, String token) throws JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = solverManagerAddress + "/problem/" + ProblemID + "/solvers";
            SolverBody[] solvers = restTemplate.getForObject(url, SolverBody[].class);
            String jsonData = objectMapper.writeValueAsString(solvers);
            return jsonData;
        } else
            return "Authorization error!";
    }

    public Solution getResultOfTask(String ProblemID, String token)
            throws JsonMappingException, JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = dbServiceAddress + "/solutions/" + ProblemID;
            Solution solution = restTemplate.getForObject(url, Solution.class);
            return solution;
        } else {
            System.out.println("Authorization error!");
            return null;
        }
    }

    public void cancelTasksForUser(CancelUserTasksRequest cancelUserTaskRequest, String token)
            throws JsonMappingException, JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = solverManagerAddress + "/cancel/user";
            restTemplate.postForEntity(url, cancelUserTaskRequest,
                    CancelUserTasksRequest.class);
        } else
            System.out.println("Authorization error!");

    }

    public String cancelTask(String TaskID, CancelTaskRequest cancelTaskRequest, String token)
            throws JsonMappingException, JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = solverManagerAddress + "/cancel/task/" + TaskID;
            restTemplate.postForEntity(url, cancelTaskRequest,
                    CancelTaskRequest.class);
            return TaskID;
        } else
            return "Authorization error!";
    }

    public String cancelSolver(String solverName, CancelSolverRequest cancelSolverRequest, String token)
            throws JsonMappingException, JsonProcessingException {
        String authRes = authenticate(token);
        if (!authRes.equals("Authorization error!")) {
            String url = solverManagerAddress + "/cancel/user";
            restTemplate.postForEntity(url, cancelSolverRequest,
                    CancelTaskRequest.class);
            return solverName;
        } else
            return "Authorization error";
    }

}
