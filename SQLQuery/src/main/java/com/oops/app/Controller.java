package com.oops.app;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.oops.app.responseType.Greeting;

@SpringBootApplication
@RestController
public class Controller 
{
    public static String ip;
    public static String username;
    public static String pwd;
    public static void main( String[] args ) {
        if(args.length < 3) {
            System.err.println("Not enough arguments to run. Make sure there is an ip/url, username and a password");
            System.exit(-1);
        }
        ip = args[0];
        username = args[1];
        pwd = args[2];
        System.out.println(ip + " " + username + " " + pwd);
        SpringApplication.run(Controller.class, args);
    }

    
    private static final String template =  "Hello, %s";
    private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting/{name}")
    public Greeting  greeting(@PathVariable String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}