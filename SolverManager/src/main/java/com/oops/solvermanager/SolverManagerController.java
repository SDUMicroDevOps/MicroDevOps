package com.oops.solvermanager;

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
import com.oops.solvermanager.Requests.CancelUserTasksRequest;
import com.oops.solvermanager.Requests.ProblemRequest;
import com.oops.solvermanager.Requests.SolverBody;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobList;
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
        try{
            createSolverJobs(newProblem);
            return ResponseEntity.status(HttpStatus.OK).body("Solvers Created for problem: " + newProblem.getProblemID());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }

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

    @PostMapping("/cancel/user")
    public ResponseEntity<String> cancelUserTasks(@RequestBody CancelUserTasksRequest req) {
        KubernetesClient api = makeKubernetesClient();
        api.batch().v1().jobs().inNamespace("default").withLabel("user", req.getUserID()).delete();
        return ResponseEntity.status(HttpStatus.OK)
                .body("Cancelled all tasks for the user: " + req.getUserID());
    }

    @GetMapping("/user/{userid}/solvers")
    public ResponseEntity<SolverBody[]> getJobsForUser(@PathVariable String userid) {
        KubernetesClient api = makeKubernetesClient();
        List<Job> running_solvers = api.batch().v1().jobs().inNamespace("default").withLabel("user", userid).list()
                .getItems();
        SolverBody[] solvers = constructSolversFromJobs(running_solvers);
        return ResponseEntity.status(HttpStatus.OK).body(solvers);
    }

    @GetMapping("/problem/{problemID}/solvers")
    public ResponseEntity<SolverBody[]> getSolversForJob(@PathVariable String problemID) {
        KubernetesClient api = makeKubernetesClient();
        List<Job> running_solvers = api.batch().v1().jobs().inNamespace("default").withLabel("problem", problemID)
                .list().getItems();
        SolverBody[] solvers = constructSolversFromJobs(running_solvers);
        return ResponseEntity.status(HttpStatus.OK).body(solvers);
    }

    private SolverBody[] constructSolversFromJobs(List<Job> jobs) {
        SolverBody[] solvers = new SolverBody[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            Job currentSolver = jobs.get(i);
            String solverName = currentSolver.getMetadata().getLabels().get("solver");
            int numberVCPU = Integer.parseInt(currentSolver.getMetadata().getLabels().get("numberVCPU"));
            int maxMemory = Integer.parseInt(currentSolver.getMetadata().getLabels().get("maxMemory"));
            int timeout = Integer.parseInt(currentSolver.getMetadata().getLabels().get("timeout"));
            SolverBody toInsert = new SolverBody(maxMemory, numberVCPU, timeout, solverName);
            solvers[i] = toInsert;
        }
        return solvers;
    }

    private void createSolverJobs(ProblemRequest newProblem) {
        KubernetesClient api = makeKubernetesClient();
        for (SolverBody solver : newProblem.getSolversToUse()) {
            List<String> command = new LinkedList<>();
            command.add("python3");
            command.add("Solver.py");
            command.add(solver.getSolverName());
            command.add(String.valueOf(solver.getNumberVCPU()));
            command.add(newProblem.getProblemID());
            command.add(newProblem.getDataID());
            Map<String, String> labels = new HashMap<>();
            labels.put("user", newProblem.getUserID());
            labels.put("solver", solver.getSolverName());
            labels.put("problem", newProblem.getProblemID());
            labels.put("numberVCPU", Integer.toString(solver.getNumberVCPU()));
            labels.put("timeout", Integer.toString(solver.getTimeout()));
            labels.put("maxMemory", Integer.toString(solver.getMaxMemory()));
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
                    .withName("solver")
                    .withImage("oopsaccount/solver:latest")
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
