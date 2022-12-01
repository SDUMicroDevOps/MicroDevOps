package com.oops.solvermanager;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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

import com.oops.solvermanager.Requests.ProblemRequest;
import com.oops.solvermanager.Requests.SolverBody;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1JobSpec;
import io.kubernetes.client.openapi.models.V1JobTemplateSpec;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import io.kubernetes.client.proto.V1.Container;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.openapi.models.V1PodBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;

@SpringBootApplication
@RestController
public class SolverManagerController {
    public static void main(String[] args) {

        SpringApplication.run(SolverManagerController.class, args);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createJob(@RequestBody ProblemRequest newProblem) {
        SolverBody test = newProblem.getSolversToUse()[0];
        try{
            createSolverJobs(newProblem);
    
        }catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
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
    private void createSolverJobs(ProblemRequest newProblem) throws IOException{
        CoreV1Api api = makeKubernetesClient();
        for (SolverBody solver : newProblem.getSolversToUse()){
            V1ObjectMeta meta = new V1ObjectMeta();
            meta.name("solver");
            Map<String,String> labels = new HashMap<>();
            labels.put("user", newProblem.getUserID());
            labels.put("problem",newProblem.getProblemID());
            labels.put("solver_name", solver.getSolverName());
            meta.labels(labels);
            V1Job jobBody = new V1Job();
            jobBody.apiVersion("v1");
            jobBody.kind("Job");
            jobBody.metadata(meta);
            V1Container container = new V1Container();
            container.name("SolverContainer");
            container.image("perl:5.34.0");
            List<String> command = new LinkedList<>();
            command.add("perl");
            command.add("-Mbignum=bpi");
            command.add("-wle");
            command.add("print bpi(2000)");
            container.command(command);
            V1JobSpec jobSpec = new V1JobSpec();
            V1PodTemplateSpec podTemplateSpec = new V1PodTemplateSpec();
            V1PodSpec podSpec = new V1PodSpec();
            podSpec.containers(Arrays.asList(container));
            podSpec.restartPolicy("Never");
            podTemplateSpec.spec(podSpec);
            jobSpec.template(podTemplateSpec);
            jobSpec.backoffLimit(4);
            jobBody.spec(jobSpec);
            
        }
    }
    private CoreV1Api makeKubernetesClient() throws IOException{
        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        ApiClient client =
        ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(client);
        return new CoreV1Api();
        
    }
}
