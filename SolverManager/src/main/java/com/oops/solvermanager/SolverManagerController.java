package com.oops.solvermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oops.solvermanager.Requests.ProblemRequest;
import com.oops.solvermanager.Requests.SolverBody;

@SpringBootApplication
@RestController
public class SolverManagerController {
    public static void main(String[] args) {

        SpringApplication.run(SolverManagerController.class, args);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createJob(@RequestBody ProblemRequest newProblem) {
        SolverBody test = newProblem.getSolversToUse()[0];
        return ResponseEntity.status(HttpStatus.OK).body(test.getSolverName());
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @PostMapping("/cancel/task/{taskID}")
    public ResponseEntity<String> cancelTask(@PathVariable String taskID){
        
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
