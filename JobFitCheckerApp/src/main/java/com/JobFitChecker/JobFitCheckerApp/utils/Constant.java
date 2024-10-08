package com.JobFitChecker.JobFitCheckerApp.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Constant {
    public static final String S3_BUCKET_NAME = "bucket1725213899633";
    public static final String SQS_QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/891612549148/putResumeSQS";
    public static final String OPENAI_API_KEY = Dotenv.configure()
            .directory("/Users/tiansigu/Documents/CS601/JobFitChecker/JobFitCheckerApp/.env") // change path to the .env file path
            .load().get("OPEN_AI_API_KEY");
}
