package com.JobFitChecker.JobFitCheckerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JobFitCheckerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobFitCheckerAppApplication.class, args);
	}

}
