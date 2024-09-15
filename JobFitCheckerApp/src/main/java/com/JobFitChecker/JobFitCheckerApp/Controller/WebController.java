package com.JobFitChecker.JobFitCheckerApp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value = { "/", "/{path:[^.]*}" })
    public String forwardToIndex() {
        // Forward all non-API requests to index.html
        return "forward:/index.html";
    }
}
