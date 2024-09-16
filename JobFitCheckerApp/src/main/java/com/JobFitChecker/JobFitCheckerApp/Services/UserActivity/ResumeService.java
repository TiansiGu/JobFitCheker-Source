package com.JobFitChecker.JobFitCheckerApp.Services.UserActivity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static com.JobFitChecker.JobFitCheckerApp.utils.Constant.S3_BUCKET_NAME;
import static com.JobFitChecker.JobFitCheckerApp.utils.Constant.S3_THREAD_NUM;

@Service
public final class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);
    private final S3AsyncClient s3AsyncClient;
    private final ExecutorService executorService;

    @Autowired
    public ResumeService(S3AsyncClient s3AsyncClient) {
        this.s3AsyncClient = s3AsyncClient;
        executorService = Executors.newFixedThreadPool(S3_THREAD_NUM);
    }

    public CompletableFuture<PutObjectResponse> putResume(MultipartFile file) throws IOException {
        String key = file.getOriginalFilename(); //ToDo: Add userId to key after login is established

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            AsyncRequestBody asyncRequestBody = AsyncRequestBody.fromInputStream(
                    file.getInputStream(),
                    file.getSize(),
                    executorService
            );

            CompletableFuture<PutObjectResponse> response = s3AsyncClient.putObject(putRequest, asyncRequestBody);

            return response.whenComplete((resp, ex) -> {
                if (ex != null) {
                    throw new RuntimeException("Failed to upload file", ex);
                }
            });
        } catch (IOException ex) {
            log.error("Unable to create async request from file" + ex);
            throw ex;
        }
    }
}
