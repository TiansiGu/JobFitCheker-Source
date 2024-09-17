package com.JobFitChecker.JobFitCheckerApp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Use @RestController instead of @Controller
public class WebController {

    @GetMapping(value = { "/", "/{path:[^.]*}" })
    public String forwardToIndex() {
        // Forward all non-API requests to index.html
        return "Default showup";
    }

    @GetMapping("/welcome")
    public String welcome() {
        // Return plain text or JSON that can be consumed by the frontend
        return "Welcome to JobFitChecker!";
    }
}
