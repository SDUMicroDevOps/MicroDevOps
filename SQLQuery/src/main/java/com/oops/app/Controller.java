package com.oops.app;

import java.sql.Date;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oops.app.requestType.SolverRequest;
import com.oops.app.requestType.VCPULimit;
import com.oops.app.requestType.PrivilageRequest;
import com.oops.app.responseType.Solver;
import com.oops.app.responseType.TaskQueue;
import com.oops.app.responseType.Privilage;
import com.oops.app.responseType.Solution;
import com.oops.app.responseType.User;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@SpringBootApplication
@RestController
public class Controller {
    private static SQLController sqlController = new SQLController();
    public static void main( String[] args ) {
        SpringApplication.run(Controller.class, args);
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return sqlController.getAllUser();
    }

    @GetMapping("/users/{username}")
    @ResponseBody
    public ResponseEntity<User> getUserByName(@PathVariable String username) {
        User user = sqlController.getUser(username);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{username}")
    @ResponseBody
    public ResponseEntity<User> changeVCPULimit(@PathVariable String username, @RequestBody VCPULimit newVCPULimit) {
        int status = sqlController.changeVCPULimit(username, newVCPULimit.getVCPULimit());
        switch (status) {
            case 200:
                return new ResponseEntity<>(HttpStatus.OK);
            
            case 400:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                
            default:
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content)
    })
    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        User user = sqlController.addUser(newUser);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{username}")
    @ResponseBody
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        int status = sqlController.deleteUser(username);

        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            case 400: return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }

    @GetMapping("/privilages")
    public List<Privilage> allPrivilages() {
        return sqlController.getAllPrivilages();
    }

    @GetMapping("/privilages/{id}")
    @ResponseBody
    public ResponseEntity<Privilage> getPrivilageById(@PathVariable int id) {
        Privilage privilage = sqlController.getPrivilage(id);
        if(privilage == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Privilage>(privilage, HttpStatus.OK);
    }

    @PostMapping("/privilages")
    @ResponseBody
    public ResponseEntity<Privilage> addPrivilage(@RequestBody PrivilageRequest newRoleName) {
        Privilage privilage = sqlController.addPrivilage(newRoleName.getRoleName());
        if(privilage == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/privilages/{id}")
    @ResponseBody
    public ResponseEntity<Privilage> deletePrivilage(@PathVariable int id) {
        int status = sqlController.deletePrivilage(id);
        
        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            case 400: return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/solvers")
    public List<Solver> allSolvers() {
        return sqlController.getAllSolvers();
    }

    @GetMapping("solvers/{id}")
    @ResponseBody
    public ResponseEntity<Solver> getSolverById(@PathVariable int id) {
        Solver solver = sqlController.getSolver(id);
        if(solver == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Solver>(solver, HttpStatus.OK);
    }

    @PostMapping("/solvers")
    @ResponseBody
    public ResponseEntity<Solver> addSolver(@RequestBody SolverRequest newSolverName) {
        Solver solver = sqlController.addSolver(newSolverName.getSolverName());
        if(solver == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/solvers/{id}")
    @ResponseBody
    public ResponseEntity<Solver> deleteSolver(@PathVariable int id) {
        int status = sqlController.deleteSolver(id);

        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            case 400: return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/solvers/byname")
    @ResponseBody
    public ResponseEntity<Solver> getSolverByName(@RequestParam(value = "name", defaultValue = "") String name) {
        Solver solver = sqlController.getSolverByName(name);
        if(solver == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Solver>(solver, HttpStatus.OK);
    }

    @GetMapping("/solutions")
    public List<Solution> allSolutions() {
        return sqlController.getAllSolutions();
    }

    @GetMapping("solutions/{taskId}")
    @ResponseBody
    public ResponseEntity<Solution> getSolutionById(@PathVariable String taskId) {
        Solution solution = sqlController.getSolution(taskId);
        if(solution == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Solution>(solution, HttpStatus.OK);
    }

    @PostMapping("/solutions/{taskId}")
    @ResponseBody
    public ResponseEntity<Solution> addSolution(@PathVariable String taskId, @RequestParam(value = "user") String user, @RequestParam(value = "content") String content, @RequestParam(value = "isOptimal") String isOptimal) {
        System.out.println(taskId);
        System.out.println(user);
        System.out.println(content);
        System.out.println(isOptimal);
        int status = sqlController.deleteSolution(taskId);
        if(status == 400) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Date date = new Date(System.currentTimeMillis());
        System.out.println(date.toString());
        Solution solution = sqlController.addSolution(new Solution(taskId, user, content, date, isOptimal.equals("true")?true:false));
        if(solution == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/solutions/{taskId}")
    @ResponseBody
    public ResponseEntity<Solution> deleteSolution(@PathVariable String taskId) {
        int status = sqlController.deleteSolution(taskId);

        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            case 400: return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/tasks")
    @ResponseBody
    public ResponseEntity<List<TaskQueue>> allQueuedTask(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "task_id", defaultValue = "") String taskId) {
        // /tasks endpoint was called with both username and task_id difined
        // which is not allowed.
        if(username.length() != 0 && taskId.length() != 0) return new ResponseEntity<List<TaskQueue>>(HttpStatus.BAD_REQUEST);

        List<TaskQueue> tasks;
        //Username was defined so only get by specific user orderd by timestamo
        if(username.length() != 0) {
            tasks = getQueuedTaskByUser(username);
        } 
        //task_id was defined so only get by specific task orderd by timestamo
        else if(taskId.length() != 0) {
            tasks = getQueuedTaskByTaskId(taskId);
        } 
        //Nothing was difined so get all queued tasks orderd by username
        else {
            tasks = getAllQueuedTask();
        }
        return new ResponseEntity<List<TaskQueue>>(tasks, HttpStatus.OK);
    }

    private List<TaskQueue> getQueuedTaskByUser(String username) {
        return sqlController.getQueuedTaskByUser(username);
    }

    private List<TaskQueue> getQueuedTaskByTaskId(String taskId) {
        return sqlController.getQueuedTaskByTaskId(taskId);
    }

    private List<TaskQueue> getAllQueuedTask() {
        return sqlController.getAllQueuedTask();
    }

    @PostMapping("/tasks")
    @ResponseBody
    public ResponseEntity<TaskQueue> addTask(@RequestBody TaskQueue taskQueue) {
        int status = sqlController.addTask(taskQueue);
        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            case 400: return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @DeleteMapping("/tasks")
    @ResponseBody
    public ResponseEntity<TaskQueue> deleteTask(@RequestParam(value = "task_id") String taskId) {
        int status = sqlController.deleteTask(taskId);
        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            case 400: return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }
}