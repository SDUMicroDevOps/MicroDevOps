package com.oops.solvermanager;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oops.solvermanager.Requests.CancelSolverRequest;
import com.oops.solvermanager.Requests.CancelTaskRequest;
import com.oops.solvermanager.Requests.ProblemRequest;
import com.oops.solvermanager.Requests.SolverBody;

import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@SpringBootApplication
@RestController
public class SolverManagerController {
    public static void main(String[] args) {

        SpringApplication.run(SolverManagerController.class, args);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createJob(@RequestBody ProblemRequest newProblem) {
        SolverBody test = newProblem.getSolversToUse()[0];
        createSolverJobs(newProblem);
        return ResponseEntity.status(HttpStatus.OK).body(test.getSolverName());
    }

    @PostMapping("/cancel/task/{problemID}")
    public ResponseEntity<String> cancelTask(@PathVariable String problemID, @RequestBody CancelTaskRequest req) {
        KubernetesClient api = makeKubernetesClient(); // TODO make the user auth connect to the database, to check
                                                       // whether or not the user is an admin
        api.batch().v1().jobs().inNamespace("default").withLabel("problem", problemID)
                .withLabel("user", req.getUserId()).delete();
        return ResponseEntity.status(HttpStatus.OK).body("Problem cancelled with ID : " + problemID);
    }

    @PostMapping("/cancel/solver/{solverName}")
    public ResponseEntity<String> cancelSolver(@PathVariable String solverName, @RequestBody CancelSolverRequest req) {
        KubernetesClient api = makeKubernetesClient();
        api.batch().v1().jobs().inNamespace("default").withLabel("problem", req.getProblemID())
                .withLabel("solver", solverName)
                .withLabel("user", req.getUserID()).delete();
        return ResponseEntity.status(HttpStatus.OK)
                .body("Cancelled solver with name " + solverName + " on task" + req.getProblemID());
    }

    private void createSolverJobs(ProblemRequest newProblem) {
        KubernetesClient api = makeKubernetesClient();
        for (SolverBody solver : newProblem.getSolversToUse()) {
            List<String> command = new LinkedList<>();
            command.add("perl");
            command.add("-Mbignum=bpi");
            command.add("-wle");
            command.add("print bpi(2000)");
            Map<String, String> labels = new HashMap<>();
            labels.put("user", newProblem.getUserID());
            labels.put("solver", solver.getSolverName());
            labels.put("problem", newProblem.getProblemID());
            Job job = new JobBuilder()
                    .withApiVersion("v1")
                    .withNewMetadata()
                    .withName(newProblem.getProblemID().toLowerCase() + solver.getSolverName().toLowerCase())
                    .withLabels(labels)
                    .endMetadata()
                    .withNewSpec()
                    .withTtlSecondsAfterFinished(30)
                    .withNewTemplate()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("pi")
                    .withImage("perl:5.34.0")
                    .withCommand(command)
                    .endContainer()
                    .withRestartPolicy("Never")
                    .endSpec()
                    .endTemplate()
                    .withBackoffLimit(4)
                    .endSpec()
                    .build();
            api.batch().v1().jobs().inNamespace("default").resource(job).create();
        }
    }

    private KubernetesClient makeKubernetesClient() {
        return new KubernetesClientBuilder().build();

    }
}
