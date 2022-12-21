package com.oops.backend.Backend.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.oops.backend.Backend.models.Solution;
import com.oops.backend.Backend.models.User;
import com.oops.backend.Backend.requests.CancelSolverRequest;
import com.oops.backend.Backend.requests.CancelTaskRequest;
import com.oops.backend.Backend.requests.CancelUserTasksRequest;
import com.oops.backend.Backend.requests.SolveRequest;
import com.oops.backend.Backend.services.BackendService;

@CrossOrigin
@RestController
public class BackendController {

    public BackendController() {
    }

    private BackendService backendService = new BackendService();

    // @GetMapping("/authTest")
    // public ResponseEntity<String> testHeader(@RequestHeader("Authorization")
    // String bearerToken) {
    // backendService.testAuth(bearerToken);
    // System.out.println(bearerToken);
    // return new ResponseEntity<String>("bearer token: " + bearerToken,
    // HttpStatus.OK);
    // }

    @GetMapping("/Users")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String bearerToken) {
        List<User> users;
        try {
            users = backendService.getUsers(bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<List<User>>(HttpStatus.I_AM_A_TEAPOT);
        }

    }

    @PostMapping("/Solve")
    public ResponseEntity<String> startSolvers(@RequestBody SolveRequest request,
            @RequestHeader("Authorization") String bearerToken)
            throws IOException, InterruptedException {
        System.out.println(request.getProblemID());
        System.out.println(request.getSolversToUse().length);
        String problemID = request.getProblemID();
        backendService.postSolversToSolverManager(request, bearerToken);

        return new ResponseEntity<String>(problemID, HttpStatus.OK);

    }

    @GetMapping("/ProblemInstance/{ProblemID}")
    public ResponseEntity<byte[]> getMZNData(@PathVariable String ProblemID,
            @RequestHeader("Authorization") String bearerToken) {
        byte[] mznData;
        try {
            mznData = backendService.getMznData(ProblemID, bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(mznData);
        } catch (IOException e) {
            return new ResponseEntity<byte[]>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @PostMapping("/ProblemInstance")
    public ResponseEntity<String> addMZNToStorage(
            @RequestPart(name = "mznFile", required = false) MultipartFile mznData,
            @RequestPart(name = "dznFile", required = false) MultipartFile dznData,
            @RequestPart String UserID, @RequestHeader("Authorization") String bearerToken)
            throws IOException, InterruptedException {

        if (mznData != null && dznData == null) {
            String problemId = backendService.addMznData(UserID, mznData, bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(problemId);
        } else if (mznData == null && dznData != null) {
            String dataID = backendService.addDznData(UserID, dznData, bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(dataID);
        } else if (mznData != null && dznData != null) {
            String problemId = backendService.addMznData(UserID, mznData, bearerToken);
            String dataID = backendService.addDznData(UserID, dznData, bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(problemId);
        }

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("No file data provided, nothing to upload");
    }

    @GetMapping("/DataInstance/{DataID}")
    public ResponseEntity<byte[]> getDZNData(@PathVariable String DataID,
            @RequestHeader("Authorization") String bearerToken) {
        byte[] dznData;
        try {
            dznData = backendService.getMznData(DataID, bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(dznData);
        } catch (IOException e) {
            return new ResponseEntity<byte[]>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/Solver")
    public ResponseEntity<String> getSolvers(@RequestHeader("Authorization") String bearerToken) {
        String solvers;
        try {
            solvers = backendService.getAllLegalSolvers(bearerToken);
            return new ResponseEntity<String>(solvers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/Solvers/user/{UserID}")
    public ResponseEntity<String> getSolversForUser(@PathVariable String UserID,
            @RequestHeader("Authorization") String bearerToken) {
        String solvers;
        try {
            solvers = backendService.getAllUserSolvers(UserID, bearerToken);
            return new ResponseEntity<String>(solvers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.I_AM_A_TEAPOT);
        }

    }

    @GetMapping("/Solvers/task/{TaskID}")
    public ResponseEntity<String> getSolversForTask(@PathVariable String ProblemID,
            @RequestHeader("Authorization") String bearerToken) {
        String solvers;
        try {
            solvers = backendService.getAllSoversForProlem(ProblemID, bearerToken);
            return new ResponseEntity<String>(solvers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/Result/{ProblemID}")
    public ResponseEntity<Solution> getResult(@PathVariable String ProblemID,
            @RequestHeader("Authorization") String bearerToken) throws JsonMappingException, JsonProcessingException {
        Solution result = backendService.getResultOfTask(ProblemID, bearerToken);
        return new ResponseEntity<Solution>(result, HttpStatus.OK);
    }

    @DeleteMapping("/Cancel/Task/{TaskID}")
    public ResponseEntity<String> cancelTask(@PathVariable String TaskID,
            @RequestBody CancelTaskRequest cancelTaskRequest, @RequestHeader("Authorization") String bearerToken)
            throws JsonMappingException, JsonProcessingException {
        String canceledTaskID = backendService.cancelTask(TaskID, cancelTaskRequest, bearerToken);
        return new ResponseEntity<String>(canceledTaskID, HttpStatus.OK);
    }

    @DeleteMapping("/Cancel/Task/{Solver}")
    public ResponseEntity<String> cancelSolver(@PathVariable String solverName,
            @RequestBody CancelSolverRequest cancelSolverRequest, @RequestHeader("Authorization") String bearerToken)
            throws JsonMappingException, JsonProcessingException {
        String canceledSolverName = backendService.cancelSolver(solverName, cancelSolverRequest, bearerToken);
        return new ResponseEntity<String>(canceledSolverName, HttpStatus.OK);
    }

    @DeleteMapping("/Cancel/User")
    public ResponseEntity<String> cancelAllTasksForUser(@RequestBody CancelUserTasksRequest cancelUserTaskRequest,
            @RequestHeader("Authorization") String bearerToken) throws JsonMappingException, JsonProcessingException {
        backendService.cancelTasksForUser(cancelUserTaskRequest, bearerToken);
        return new ResponseEntity<String>("Tasks for user cancelled", HttpStatus.OK);
    }

}
