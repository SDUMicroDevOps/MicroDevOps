package com.oops.backend.Backend.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oops.backend.Backend.models.Solver;
import com.oops.backend.Backend.requests.CancelSolverRequest;
import com.oops.backend.Backend.requests.CancelTaskRequest;
import com.oops.backend.Backend.requests.CancelUserTasksRequest;
import com.oops.backend.Backend.requests.SolveRequest;
import com.oops.backend.Backend.services.BackendService;

@RestController
public class BackendController {

    public BackendController() {
    }

    private BackendService backendService = new BackendService();

    @PostMapping("/Solve")
    public ResponseEntity<String> startSolvers(@RequestBody SolveRequest request) {
        backendService.postSolversToSolverManager(request);

        String taskID = request.getProblemID();
        return new ResponseEntity<String>(taskID, HttpStatus.OK);
    }

    @GetMapping("/ProblemInstance/{ProblemID}")
    public ResponseEntity<String> getMZNData(@PathVariable String ProblemID) {
        String mznData = backendService.getMznData(ProblemID);
        return new ResponseEntity<String>(mznData, HttpStatus.OK);
    }

    @PostMapping("/ProblemInstance")
    public ResponseEntity<String> addMZNToStorage(
            @RequestPart(name = "mznFile", required = false) MultipartFile mznData,
            @RequestPart(name = "dznFile", required = false) MultipartFile dznData,
            @RequestPart String UserID) throws IOException, InterruptedException {

        if (mznData != null && dznData == null) {
            String problemId = backendService.addMznData(UserID, mznData);
            ResponseEntity.status(HttpStatus.OK).body(problemId);
        } else if (mznData == null && dznData != null) {
            String dataID = backendService.addDznData(UserID, dznData);
            ResponseEntity.status(HttpStatus.OK).body(dataID);
        } else if (mznData != null && dznData != null) {
            String problemId = backendService.addMznData(UserID, mznData);
            String dataID = backendService.addDznData(UserID, dznData);
            ResponseEntity.status(HttpStatus.OK).body(problemId);
        }

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("No file data provided, nothing to upload");
    }

    @GetMapping("/DataInstance/{ProblemID}")
    public ResponseEntity<String> getDZNData(@PathVariable String ProblemID) {
        String dznData = backendService.getDznData(ProblemID);
        return new ResponseEntity<String>(dznData, HttpStatus.OK);
    }

    @GetMapping("/Solver")
    public ResponseEntity<String> getSolvers() {
        String solvers;
        try {
            solvers = backendService.getAllLegalSolvers();
            return new ResponseEntity<String>(solvers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/Solvers/user/{UserID}")
    public ResponseEntity<String> getSolversForUser(@PathVariable String UserID) {
        String solvers;
        try {
            solvers = backendService.getAllUserSolvers(UserID);
            return new ResponseEntity<String>(solvers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.I_AM_A_TEAPOT);
        }

    }

    @GetMapping("/Solvers/task/{TaskID}")
    public ResponseEntity<String> getSolversForTask(@PathVariable String ProblemID) {
        String solvers;
        try {
            solvers = backendService.getAllSoversForProlem(ProblemID);
            return new ResponseEntity<String>(solvers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/Result/{ProblemID}")
    public ResponseEntity<String> getResult(@PathVariable String ProblemID) {
        String result = backendService.getResultOfTask(ProblemID);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @DeleteMapping("/Cancel/Task/{TaskID}")
    public ResponseEntity<String> cancelTask(@PathVariable String TaskID,
            @RequestBody CancelTaskRequest cancelTaskRequest) {
        String canceledTaskID = backendService.cancelTask(TaskID, cancelTaskRequest);
        return new ResponseEntity<String>(canceledTaskID, HttpStatus.OK);
    }

    @DeleteMapping("/Cancel/Task/{Solver}")
    public ResponseEntity<String> cancelSolver(@PathVariable String solverName,
            @RequestBody CancelSolverRequest cancelSolverRequest) {
        String canceledSolverName = backendService.cancelSolver(solverName, cancelSolverRequest);
        return new ResponseEntity<String>(canceledSolverName, HttpStatus.OK);
    }

    @DeleteMapping("/Cancel/User")
    public ResponseEntity<String> cancelAllTasksForUser(@RequestBody CancelUserTasksRequest cancelUserTaskRequest) {
        backendService.cancelTasksForUser(cancelUserTaskRequest);
        return new ResponseEntity<String>("Tasks for user cancelled", HttpStatus.OK);
    }

}
