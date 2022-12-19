package com.oops.solvermanager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Date;
import java.time.Instant;
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

import com.google.gson.Gson;
import com.oops.solvermanager.Requests.CancelSolverRequest;
import com.oops.solvermanager.Requests.CancelTaskRequest;
import com.oops.solvermanager.Requests.CancelUserTasksRequest;
import com.oops.solvermanager.Requests.ProblemRequest;
import com.oops.solvermanager.Requests.SolutionFound;
import com.oops.solvermanager.Requests.SolverBody;
import com.oops.solvermanager.Responses.User;
import com.oops.solvermanager.models.Solver;
import com.oops.solvermanager.models.Task;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@SpringBootApplication
@RestController
public class SolverManagerController {
    private static final String databaseManagerService ="http://" + System.getenv().getOrDefault("DB_MANAGER_SERVICE",
            "localhost");
    private static final String databaseManagerPort = System.getenv().getOrDefault("DB_MANAGER_PORT", "8082");

    public static void main(String[] args) {

        SpringApplication.run(SolverManagerController.class, args);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createJob(@RequestBody ProblemRequest newProblem) {
        try {
            int requestedCpus = getCpuForRequest(newProblem);
            int cpusAvailableForUser = getCpuAvailableForUser(newProblem.getUserID());
            if (requestedCpus <= cpusAvailableForUser) {
                if (requestedCpus <= cpusAvailableForUser - getCpuUsedByUser(newProblem.getUserID())) {
                    createSolverJobs(newProblem);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Solvers Created for problem: " + newProblem.getProblemID());
                } else {
                    queueProblemRequest(newProblem);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Solvers queued for problem: " + newProblem.getProblemID());
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough VCPU's to handle request");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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

    @PostMapping("/solution/{taskID}") // TODO actually implement this
    public ResponseEntity<String> solutionFound(@PathVariable String taskID, @RequestBody SolutionFound req)
            throws Exception {
        CancelTaskRequest cancelReq = new CancelTaskRequest(req.getUserID());
        cancelTask(taskID, cancelReq);
        fetchJobFromQueue(req.getUserID());
        return ResponseEntity.status(HttpStatus.OK).body("Removing other solvers working on task: " + taskID);
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

    private void fetchJobFromQueue(String userID) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(databaseManagerService + ":" + databaseManagerPort + "/tasks/?username=" + userID))
                .GET()
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        Map<String, Integer> vcpuMap = new HashMap<>();
        Map<String, List<SolverBody>> solverMap = new HashMap<>();
        for (Task task : tasks) {
            String solverName = getSolverNameFromId(task.getSolver());
            if (vcpuMap.containsKey(task.getTaskId())) {
                vcpuMap.put(task.getTaskId(), task.getVcpu() + vcpuMap.get(task.getTaskId()));
                solverMap.get(task.getTaskId())
                        .add(new SolverBody(task.getMaxMemory(), task.getVcpu(), 1000, solverName)); // TODO Get
                                                                                                     // Steven
                                                                                                     // to add
                                                                                                     // timeout
                                                                                                     // to the
                                                                                                     // database

            } else {
                vcpuMap.put(task.getTaskId(), task.getVcpu());
                solverMap.put(task.getTaskId(), new LinkedList<SolverBody>());
                solverMap.get(task.getTaskId())
                        .add(new SolverBody(task.getMaxMemory(), task.getVcpu(), 1000, solverName));
            }
        }
        int cpuAvailable = getCpuAvailableForUser(userID) - getCpuUsedByUser(userID);

        for (String taskid : vcpuMap.keySet()) {
            if (vcpuMap.get(taskid) <= cpuAvailable) {
                SolverBody[] toUse = (SolverBody[]) solverMap.get(taskid).toArray();
                ProblemRequest problemRequest = new ProblemRequest(taskid, taskid, toUse, userID);
                createJob(problemRequest);
                cpuAvailable -= vcpuMap.get(taskid);// TODO delete this from database
            }
        }
    }

    private void createSolverJobs(ProblemRequest newProblem) throws Exception {
        for (SolverBody solver : newProblem.getSolversToUse()) {
            createSolverJob(solver, newProblem);
        }
    }

    private void queueProblemRequest(ProblemRequest newProblem) throws Exception {
        for (SolverBody solver : newProblem.getSolversToUse()) {
            addJobToQueue(solver, newProblem);
        }
    }

    private int getCpuAvailableForUser(String userId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(databaseManagerService + ":" + databaseManagerPort + "/users/" + userId))
                .GET()
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        User user = gson.fromJson(response.body(), User.class);
        return user.getVcpulimit();
    }

    private int getCpuUsedByUser(String userId) {
        KubernetesClient api = makeKubernetesClient();
        List<Job> running_solvers = api.batch().v1().jobs().inNamespace("default").withLabel("user", userId)
                .list().getItems();
        SolverBody[] solvers = constructSolversFromJobs(running_solvers);
        int sumCpu = 0;
        for (SolverBody solver : solvers) {
            sumCpu += solver.getNumberVCPU();
        }
        return sumCpu;
    }

    private String getSolverNameFromId(int solverID) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(
                        databaseManagerService + ":" + databaseManagerPort + "/solvers/" + Integer.toString(solverID)))
                .GET()
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        Solver solver = gson.fromJson(response.body(), Solver.class);
        return solver.getSolverName();
    }

    private KubernetesClient makeKubernetesClient() {
        return new KubernetesClientBuilder().build();
    }

    private void createSolverJob(SolverBody solver, ProblemRequest problem) {
        KubernetesClient api = makeKubernetesClient();
        List<String> command = new LinkedList<>();
        command.add("/usr/local/bin/python3");
        command.add("Solver.py");
        command.add(solver.getSolverName());
        command.add(String.valueOf(solver.getNumberVCPU()));
        command.add(problem.getUserID());
        command.add(problem.getProblemID());
        Map<String, String> labels = new HashMap<>();
        labels.put("user", problem.getUserID());
        labels.put("solver", solver.getSolverName());
        labels.put("problem", problem.getProblemID());
        labels.put("numberVCPU", Integer.toString(solver.getNumberVCPU()));
        labels.put("timeout", Integer.toString(solver.getTimeout()));
        labels.put("maxMemory", Integer.toString(solver.getMaxMemory()));
        Map<String, Quantity> requestedResources = new HashMap<>();
        requestedResources.put("cpu", Quantity.parse(Integer.toString(solver.getNumberVCPU())));
        requestedResources.put("memory", Quantity.parse(Integer.toString(solver.getMaxMemory())));

        Job job = new JobBuilder()
                .withApiVersion("v1")
                .withNewMetadata()
                .withName(problem.getProblemID().toLowerCase() + solver.getSolverName().toLowerCase())
                .withLabels(labels)
                .endMetadata()
                .withNewSpec()
                .withActiveDeadlineSeconds(Integer.toUnsignedLong(solver.getTimeout()))
                .withTtlSecondsAfterFinished(30)
                .withNewTemplate()
                .withNewSpec()
                .addNewContainer()
                .withName("solver")
                .withImage("oopsaccount/solver:latest")
                .withCommand(command)
                .withNewResources()
                .withRequests(requestedResources)
                .endResources()
                .endContainer()
                .withRestartPolicy("Never")
                .endSpec()
                .endTemplate()
                .withBackoffLimit(4)
                .endSpec()
                .build();
        api.batch().v1().jobs().inNamespace("default").resource(job).create();
    }

    private void addJobToQueue(SolverBody solver, ProblemRequest problem) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        int solverId = getIdForSolver(solver.getSolverName());
        Date currentDate = Date.from(Instant.now());
        Task task = new Task(problem.getUserID(), solverId, problem.getProblemID(), currentDate,
                solver.getMaxMemory(), "", "", solver.getNumberVCPU(),solver.getTimeout());
        String jsonTask = gson.toJson(task);
        var request = HttpRequest.newBuilder(
                URI.create(databaseManagerService + ":" + databaseManagerPort + "/tasks"))
                .POST(BodyPublishers.ofString(jsonTask))
                .header("accept", "application/json")
                .build();
        client.send(request, BodyHandlers.ofString()); // TODO, maybe make some sort of error handling here
    }

    private int getIdForSolver(String solverName) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(databaseManagerService + ":" + databaseManagerPort + "/solvers/?name=" + solverName))
                .GET()
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        Solver solver = gson.fromJson(response.body(), Solver.class);
        return solver.getId();
    }

    private int getCpuForRequest(ProblemRequest req) {
        int sum = 0;
        for (SolverBody solver : req.getSolversToUse()) {
            sum += solver.getNumberVCPU();
        }
        return sum;
    }

    public boolean simpleWiremockTest() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        var request = HttpRequest.newBuilder(
                URI.create(databaseManagerService + ":" + databaseManagerPort + "/test"))
                .GET()
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return true;

    }
}
