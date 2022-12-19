package com.oops.backend.Backend.services;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oops.backend.Backend.models.BucketResponse;
import com.oops.backend.Backend.models.Solution;
import com.oops.backend.Backend.models.Solver;
import com.oops.backend.Backend.models.SolverBody;
import com.oops.backend.Backend.models.TaskQueue;
import com.oops.backend.Backend.requests.CancelSolverRequest;
import com.oops.backend.Backend.requests.CancelTaskRequest;
import com.oops.backend.Backend.requests.CancelUserTasksRequest;
import com.oops.backend.Backend.requests.SolveRequest;

/*
 * Environment variables needed
 * 
 * SOLVER_MANAGER_SERVICE   - Local network address of Solver manager
 * SOVER_MANAGER_PORT
 * DB_SERVICE       - Address of db service 
 * DB_SERVICE_PORT
 * BUCKER_HANDLER_SERVICE   - Address of bucket handler
 * BUCKET_HANDLER_PORT
 */

@Service
public class BackendService {
    private String solverManagerAddress = "http://" + System.getenv("SOLVER_MANAGER_SERVICE") + ":"
            + System.getenv("SOLVER_MANAGER_PORT");
    private String dbServiceAddress = "http://" + System.getenv("DATABASE_SERVICE") + ":" + System.getenv("DATABASE_PORT");
    private String bucketHandlerAddress = "http://" + System.getenv("BUCKET_HANDLER_SERVICE") + ":"
            + System.getenv("BUCKET_HANDLER_PORT");

    private RestTemplate restTemplate = new RestTemplate();

    ObjectMapper objectMapper = new ObjectMapper();

    public BackendService() {
    }

    public void postSolversToSolverManager(SolveRequest request) {
        String url = solverManagerAddress + "/New";
        restTemplate.postForEntity(url, request, SolveRequest.class);
    }

    public String getAllLegalSolvers() throws JsonProcessingException {
        String url = dbServiceAddress + "/solvers";
        Solver[] solvers = restTemplate.getForObject(url, Solver[].class);
        String jsonData = objectMapper.writeValueAsString(solvers);

        return jsonData;
    }

    public String getMznData(String ProblemID) {
        String url = dbServiceAddress + "/tasks";
        TaskQueue[] taskQueue = restTemplate.getForObject(url, TaskQueue[].class);
        if (taskQueue != null) {
            for (TaskQueue task : taskQueue) {
                if (task.getTaskID().equals(ProblemID)) {
                    return task.getMzn();
                }
            }
        }
        return "";
    }

    public String addMznData(String UserID, MultipartFile mznFile) throws IOException, InterruptedException {
        String url = bucketHandlerAddress + "/TaskBucket/uploadurl/{taskID}";
        BucketResponse bucketResponse = restTemplate.getForObject(url, BucketResponse.class);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileExtension = StringUtils.cleanPath(mznFile.getOriginalFilename());
        String fileName = UserID + timestamp.getTime() + fileExtension;

        byte[] fileData = mznFile.getBytes();
        OutputStream out = new FileOutputStream(new File("/tmp/" + fileName));
        out.write(fileData);
        out.close();

        if (bucketResponse != null) {

            String uploadUrl = bucketResponse.getDataFileUrl();

            ProcessBuilder pb = new ProcessBuilder(
                    "curl",
                    "-X", "PUT",
                    "-H", "Content-Type: text/plain",
                    "--upload-file", "/tmp/" + fileName,
                    uploadUrl);

            Process p = pb.start();
            p.waitFor();
        }

        File file = new File(fileName);
        file.delete();

        return fileName.replace(fileExtension, "");
    }

    public String addDznData(String UserID, MultipartFile dznFile) throws IOException, InterruptedException {
        String url = bucketHandlerAddress + "/TaskBucket/uploadurl/{taskID}";
        BucketResponse bucketResponse = restTemplate.getForObject(url, BucketResponse.class);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String fileExtension = StringUtils.cleanPath(dznFile.getOriginalFilename());
        String fileName = UserID + timestamp.getTime() + fileExtension;

        if (bucketResponse != null) {
            String uploadUrl = bucketResponse.getDataFileUrl();

            ProcessBuilder pb = new ProcessBuilder(
                    "curl",
                    "-X", "PUT",
                    "-H", "Content-Type: text/plain",
                    "--upload-file", "/tmp/" + fileName,
                    uploadUrl);

            Process p = pb.start();
            p.waitFor();
        }
        File file = new File(fileName);
        file.delete();

        return fileName.replace(fileExtension, "");
    }

    public String getDznData(String ProblemID) {
        String url = dbServiceAddress + "/tasks";
        TaskQueue[] taskQueue = restTemplate.getForObject(url, TaskQueue[].class);
        if (taskQueue != null) {
            for (TaskQueue task : taskQueue) {
                if (task.getTaskID().equals(ProblemID)) {
                    return task.getDzn();
                }
            }
        }
        return "";
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
