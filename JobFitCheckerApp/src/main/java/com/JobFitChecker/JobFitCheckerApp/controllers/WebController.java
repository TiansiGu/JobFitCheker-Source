package com.JobFitChecker.JobFitCheckerApp.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Use @RestController instead of @Controller
@CrossOrigin
@RequestMapping("/api")
public class WebController {

    @RequestMapping("/")
    public String defaultShowup() {
        return "Default page for back end";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to JobFitChecker!";
    }
}
