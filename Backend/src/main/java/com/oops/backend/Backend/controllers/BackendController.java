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
    public String startSolvers(@RequestBody SolveRequest request) {
        backendService.postSolversToSolverManager(request);

        String taskID = UUID.randomUUID().toString();
        return taskID;
    }

    @GetMapping("/ProblemInstance/{ProblemID}")
    public String getMZNData(@PathVariable String ProblemID) {
        return backendService.getMznData(ProblemID);
    }

    @PostMapping("/ProblemInstance")
    public ResponseEntity<String> addMZNToStorage(@RequestPart(name = "file", required = false) MultipartFile mznData,
            @RequestPart(name = "file", required = false) MultipartFile dznData,
            @RequestPart String UserID) throws IOException, InterruptedException {

        if (dznData == null && mznData != null) {
            String problemID = backendService.addMznData(UserID, mznData);
            ResponseEntity.status(HttpStatus.OK).body(problemID);

        } else if (mznData == null && dznData != null) {
            String dataID = backendService.addDznData(UserID, dznData);
            ResponseEntity.status(HttpStatus.OK).body(dataID);

        }

        return ResponseEntity.status(HttpStatus.OK).body("MZN data or DZN data not specified or null");
    }

    @GetMapping("/ProblemInstance/{ProblemID}")
    public String getDZNData(@PathVariable String ProblemID) {
        return backendService.getDznData(ProblemID);
    }

    @GetMapping
    public ResponseEntity<String> getSolvers() throws JsonProcessingException {
        String solvers = backendService.getAllLegalSolvers();
        return ResponseEntity.status(HttpStatus.OK).body(solvers);
    }

    @GetMapping("/Solvers/{UserID}")
    public ResponseEntity<String> getSolversForUser(@PathVariable String UserID) throws JsonProcessingException {
        String solvers = backendService.getAllUserSolvers(UserID);
        return ResponseEntity.status(HttpStatus.OK).body(solvers);
    }

    @GetMapping("/Solvers/{TaskID}")
    public ResponseEntity<String> getSolversForTask(@PathVariable String ProblemID) throws JsonProcessingException {
        String solvers = backendService.getAllSoversForProlem(ProblemID);
        return ResponseEntity.status(HttpStatus.OK).body(solvers);
    }

    @GetMapping("/Result/{ProblemID}")
    public ResponseEntity<String> getResult(@PathVariable String ProblemID) {
        String result = backendService.getResultOfTask(ProblemID);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/Cancel/Task/{TaskID}")
    public void cancelTask(@PathVariable String TaskID, @RequestBody CancelTaskRequest cancelTaskRequest) {
        backendService.cancelTask(TaskID, cancelTaskRequest);
    }

    @DeleteMapping("/Cancel/Task/{Solver}")
    public void cancelSolver(@PathVariable Solver solver, @RequestBody CancelSolverRequest cancelSolverRequest) {
        backendService.cancelSolver(solver, cancelSolverRequest);
    }

    @DeleteMapping("/Cancel/User")
    public void cancelAllTasksForUser(@RequestBody CancelUserTasksRequest cancelUserTaskRequest) {
        backendService.cancelTasksForUser(cancelUserTaskRequest);
    }

}
