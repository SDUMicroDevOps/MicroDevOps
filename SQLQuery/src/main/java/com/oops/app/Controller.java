package com.oops.app;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oops.app.exceptions.BadRequestException;
import com.oops.app.requestType.AvailableSolverRequest;
import com.oops.app.requestType.PrivilageRequest;
import com.oops.app.requestType.SolutionRequest;
import com.oops.app.responseType.AvailableSolver;
import com.oops.app.responseType.Greeting;
import com.oops.app.responseType.Privilage;
import com.oops.app.responseType.Solution;
import com.oops.app.responseType.User;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@SpringBootApplication
@RestController
public class Controller {
    public static SQLController sqlController;
    public static void main( String[] args ) {
        if(args.length < 3) {
            System.err.println("Not enough arguments to run. Make sure there is an ip/url, username and a password");
            System.exit(-1);
        }
        sqlController  = new SQLController(args[0], "oops_test", args[1], args[2]);
        SpringApplication.run(Controller.class, args);
    }

    
    private static final String template =  "Hello, %s";
    private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting/{name}")
    public Greeting  greeting(@PathVariable String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
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
            
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
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
            throw new BadRequestException();
        }
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{username}")
    @ResponseBody
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        int status = sqlController.deleteUser(username);

        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
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

        }
        return new ResponseEntity<Privilage>(privilage, HttpStatus.OK);
    }

    @PostMapping("/privilages")
    @ResponseBody
    public ResponseEntity<Privilage> addPrivilage(@RequestBody PrivilageRequest newRoleName) {
        Privilage privilage = sqlController.addPrivilage(newRoleName.getRoleName());
        if(privilage == null){

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/privilages/{id}")
    @ResponseBody
    public ResponseEntity<Privilage> deletePrivilage(@PathVariable int id) {
        int status = sqlController.deletePrivilage(id);
        
        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/solvers")
    public List<AvailableSolver> allSolvers() {
        return sqlController.getAllSolvers();
    }

    @GetMapping("solvers/{id}")
    @ResponseBody
    public ResponseEntity<AvailableSolver> getSolverById(@PathVariable int id) {
        AvailableSolver solver = sqlController.getSolver(id);
        if(solver == null) {

        }
        return new ResponseEntity<AvailableSolver>(solver, HttpStatus.OK);
    }

    @PostMapping("/solvers")
    @ResponseBody
    public ResponseEntity<AvailableSolver> addSolver(@RequestBody AvailableSolverRequest newSolverName) {
        AvailableSolver solver = sqlController.addSolver(newSolverName.getSolverName());
        if(solver == null) {

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/solvers/{id}")
    @ResponseBody
    public ResponseEntity<AvailableSolver> deleteSolver(@PathVariable int id) {
        int status = sqlController.deleteSolver(id);

        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/solutions")
    public List<Solution> allSolutions() {
        return sqlController.getAllSolutions();
    }

    @GetMapping("solutions/{id}")
    @ResponseBody
    public ResponseEntity<Solution> getSolutionById(@PathVariable int id) {
        Solution solution = sqlController.getSolution(id);
        if(solution == null) {

        }
        return new ResponseEntity<Solution>(solution, HttpStatus.OK);
    }

    @PostMapping("/solutions")
    @ResponseBody
    public ResponseEntity<Solution> addSolution(@RequestBody SolutionRequest newsolutionName) {
        Solution solution = sqlController.addSolution(newsolutionName);
        if(solution == null) {

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/solutions/{id}")
    @ResponseBody
    public ResponseEntity<Solution> deleteSolution(@PathVariable int id) {
        int status = sqlController.deleteSolution(id);

        switch (status) {
            case 200: return new ResponseEntity<>(HttpStatus.OK);
            default: return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }
}