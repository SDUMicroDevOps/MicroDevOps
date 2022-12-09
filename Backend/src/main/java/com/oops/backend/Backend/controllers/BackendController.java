package com.oops.backend.Backend.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.oops.backend.Backend.requests.SolveRequest;
import com.oops.backend.Backend.services.SolveService;

@RestController
public class BackendController {
    public BackendController() {
    }

    private SolveService solveService = new SolveService(WebClient.builder());

    @PostMapping
    public String startSolvers(@RequestBody SolveRequest request) {
        solveService.postSolversToSolverManager(request);

        String taskID = UUID.randomUUID().toString();
        return taskID;
    }
}
