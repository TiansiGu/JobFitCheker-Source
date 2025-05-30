package com.JobFitChecker.ResumePostProcessor.resumePostProcessWorkflow;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public final class ResumePostProcessor {

    private static final Logger log = LoggerFactory.getLogger(ResumePostProcessor.class);

    private final PostProcessingTask processingTask;
    private final TaskExecutor taskExecutor;

    @Autowired
    public ResumePostProcessor(TaskExecutor taskExecutor, PostProcessingTask processingTask) {
        this.taskExecutor = taskExecutor;
        this.processingTask = processingTask;
    }

    /**
     * Start the task as soon as the initialization of bean properties.
     * https://www.baeldung.com/spring-postconstruct-predestroy
     * Start a background thread, the processing workflow continuously runs throughout
     * the application's lifecycle in a background thread to poll, extract, and store
     * resume metadata, and would not block other requests.
     */
    @PostConstruct
    public void startProcessing() {
        log.info("Starting ResumePostProcessing...");
        taskExecutor.execute(processingTask);
    }

    /**
     * The thread would shut down gracefully when stopping the application.
     * Runs only once before Spring removes our bean from the application context.
     */
    @PreDestroy
    public void stopProcessing() {
        log.info("Stopping ResumePostProcessing...");
        processingTask.stop();
    }

    @Component
    public static class PostProcessingTask implements Runnable {
        private volatile boolean isRunning = true; // visibility
        private final PollResumeActivity pollResumeActivity;
        private final ExtractQualificationActivity extractQualificationActivity;
        private final UpdateQualificationActivity updateQualificationActivity;

        @Autowired
        public PostProcessingTask(
                PollResumeActivity pollResumeActivity,
                ExtractQualificationActivity extractQualificationActivity,
                UpdateQualificationActivity updateQualificationActivity
        ) {
            this.pollResumeActivity = pollResumeActivity;
            this.extractQualificationActivity = extractQualificationActivity;
            this.updateQualificationActivity = updateQualificationActivity;
        }


        @Override
        public void run() {
            while (isRunning) {
                try {
                    List<Map.Entry<Long, String>> resumeTexts = pollResumeActivity.pollFromSqsQueue();

                    for (Map.Entry<Long, String> resumeTextEntry : resumeTexts) {
                        long userId = resumeTextEntry.getKey();
                        String resumeText = resumeTextEntry.getValue();

                        Qualification qualification = extractQualificationActivity.extractDataFromResumeText(userId, resumeText);

                        updateQualificationActivity.updateQualification(qualification);
                    }

                } catch (Exception ex) {
                    log.error("Could not extract and update resume metadata: " + ex);
                }
            }
        }

        public void stop() {
            isRunning = false;
        }
    }
}
