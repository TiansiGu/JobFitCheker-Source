package com.JobFitChecker.JobFitCheckerApp.services.userActivity;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.JobFitChecker.JobFitCheckerApp.model.User;
import com.JobFitChecker.JobFitCheckerApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import static com.JobFitChecker.JobFitCheckerApp.utils.Constant.S3_BUCKET_NAME;

@Service
public final class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final S3Client s3Client;
    private UserRepository userRepository;

    @Autowired
    public ResumeService(UserRepository userRepository, S3Client s3Client) {
        this.userRepository = userRepository;
        this.s3Client = s3Client;
    }

    public void putResume(long userId, MultipartFile file) throws Exception {
        String resumeKey = getResumeKey(userId, file.getOriginalFilename());

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(resumeKey)
                    .contentType(file.getContentType())
                    .build();
            RequestBody requestBody = RequestBody.fromInputStream(
                    file.getInputStream(),
                    file.getSize()
            );
            s3Client.putObject(putRequest, requestBody);

            userRepository.updateResumeKey(userId, resumeKey);

        } catch (IOException ex) {
            log.error("Unable to create request body from file" + ex);
            throw ex;
        }
    }

    public void deleteResume(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            String resumeKey = optionalUser.get().getResumeKey();

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(resumeKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);

            userRepository.updateResumeKey(userId, null);
        } else {
            throw new NoSuchElementException("The user with id " + userId + "does not exist");
        }
    }

    private String getResumeKey(long userId, String fileName) {
        return userId + "@" + fileName;
    }

}
