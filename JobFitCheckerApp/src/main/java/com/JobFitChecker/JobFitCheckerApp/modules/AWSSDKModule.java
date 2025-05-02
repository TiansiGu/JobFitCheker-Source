package com.JobFitChecker.JobFitCheckerApp.modules;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AWSSDKModule {

    @Bean
    public S3Client getS3Client() {
        AwsCredentials creds = DefaultCredentialsProvider.create().resolveCredentials();
        System.out.println("Using AWS credentials: " + creds.toString());
        return S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .build();
    }

    @Bean
    public SqsClient getSqsClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
