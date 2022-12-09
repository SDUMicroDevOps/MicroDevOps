package com.oops.backend.Backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.oops.backend.Backend.requests.SolveRequest;

import reactor.core.publisher.Mono;

/*
 * Environment variables needed
 * 
 * SOLVER_MANAGER_ADDRESS   - Local network address of Solver manager
 * DB_SERVICE_ADDRESS       - Address of db service 
 */

@Service
public class SolveService {
    private String solverManagerAddress = System.getenv("SOLVER_MANAGER_ADDRESS");
    private String dbServiceAddress = System.getenv("DB_SERVICE_ADDRESS");

    private final WebClient webClient;

    public SolveService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> postSolversToSolverManager(SolveRequest request) {
        return webClient.post()
                .uri(solverManagerAddress + "/New")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getAllLegalSolvers() {
        return webClient.get()
                .uri(dbServiceAddress + "/solvers")
                .retrieve()
                .bodyToMono(String.class);
    }

}
