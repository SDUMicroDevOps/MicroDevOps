package com.oops.solutionmanager;


import com.oops.solutionmanager.models.Solution;

import com.oops.solutionmanager.requests.SolutionRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/*
 *   Required Environment variables:
 *       DB_SERVICE_ADDRESS:                         The network address of database service
 */


@SpringBootApplication
@RestController
public class SolutionManagerController {

    private static final String DBServiceAddress = System.getenv("DB_SERVICE_ADDRESS");

    public static void main(String[] args) {
        SpringApplication.run(SolutionManagerController.class, args);
    }

    @GetMapping("/Result/{taskID}")
    public List<Object> getSolution(@PathVariable String taskID) {
        RestTemplate restTemplate = new RestTemplate();
        Solution solution = restTemplate.getForObject(DBServiceAddress +"//solutions/" + taskID, Solution.class);
        if(solution == null) {
            return null;
        }
        return Arrays.asList(solution.getSolution(), solution.getOptimal());
    }

    @PostMapping("/SolutionFound")
    public ResponseEntity<String> postSolution(@RequestBody SolutionRequest solutionRequest) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Solution> response = restTemplate.postForEntity(
                DBServiceAddress + "//solutions", solutionRequest, Solution.class
        );

        if(response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.OK).body("Request successful!");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Request failed!");
        }
    }


}