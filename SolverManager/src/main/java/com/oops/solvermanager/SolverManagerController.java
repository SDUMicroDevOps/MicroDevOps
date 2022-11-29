package com.oops.solvermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oops.solvermanager.Requests.ProblemRequest;

@SpringBootApplication
@RestController
public class SolverManagerController {
    public static void main(String[] args) {

        SpringApplication.run(SolverManagerController.class, args);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createJob(@RequestBody ProblemRequest newProblem) {

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
