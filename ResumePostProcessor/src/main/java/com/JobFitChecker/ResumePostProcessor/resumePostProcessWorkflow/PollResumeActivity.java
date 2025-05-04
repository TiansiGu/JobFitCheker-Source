package com.JobFitChecker.ResumePostProcessor.resumePostProcessWorkflow;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.amazon.s3.AmazonS3DocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.JobFitChecker.ResumePostProcessor.utils.Constant.SQS_QUEUE_URL;
import static com.JobFitChecker.ResumePostProcessor.utils.Constant.S3_BUCKET_NAME;

@Component
public final class PollResumeActivity {
    private static final Logger log = LoggerFactory.getLogger(PollResumeActivity.class);

    private final SqsClient sqsClient;
    private final S3Client s3Client;

    @Autowired
    public PollResumeActivity(SqsClient sqsClient, S3Client s3Client) {
        this.sqsClient = sqsClient;  // SQS client
        this.s3Client = s3Client;    // S3 client
    }

    public List<Map.Entry<Long, String>> pollFromSqsQueue() {
        // Receive up to 10 messages at a time, with a 20-second wait if no messages are available
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(SQS_QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20) // long polling, wait 20 seconds
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
        List<Message> messages = response.messages();

        List<Map.Entry<Long, String>> resumeTexts = new ArrayList<>();
        for (Message message : messages) {
            // Extract PDF's object key from the message body (typically JSON)
            List<String> keys = extractObjectKeys(message.body(),message);
            if (keys.isEmpty()) return resumeTexts;

            // Process the S3 file
            String key = URLDecoder.decode(keys.get(0), StandardCharsets.UTF_8);
            log.info("Polled putResume message from queue: key {} ", key);

            long userId = getUserIdFromKey(key);
            String text = processS3File(S3_BUCKET_NAME, key);

            Map.Entry<Long, String> entry = new AbstractMap.SimpleEntry<>(userId, text);
            resumeTexts.add(entry);

            // Delete the message from the queue after loading the text to local
            deleteMessage(message);
            log.info("Deleted putResume message from queue: key {} ", key);
        }
        return resumeTexts;
    }

    /**
     * Use ApachePdfBox's integration with langchain4j to parse S3 pdf document to text. Reference:
     * https://github.com/langchain4j/langchain4j/blob/main/document-parsers/langchain4j-document-parser-apache-pdfbox/src/main/java/dev/langchain4j/data/document/parser/apache/pdfbox/ApachePdfBoxDocumentParser.java
     * @param bucketName S3 bucket name
     * @param objectKey S3 file object key
     * @return parsed text of S3 file (resume)
     */
    private String processS3File(String bucketName, String objectKey) {
        log.info("Converting resume {} to text: ", objectKey);

        AmazonS3DocumentLoader loader = new AmazonS3DocumentLoader(s3Client);
        DocumentParser parser = new ApachePdfBoxDocumentParser();
        Document document = loader.loadDocument(bucketName, objectKey, parser);

        return document.text();
    }

    private List<String> extractObjectKeys(String messageBody, Message message) {
        JsonObject jsonObject = JsonParser.parseString(messageBody).getAsJsonObject();
        List<String> keys = new ArrayList<>();
        JsonArray recordsArray = jsonObject.getAsJsonArray("Records");

        if (jsonObject.has("Event") && "s3:TestEvent".equals(jsonObject.get("Event").getAsString())) {
            log.info("Skipping S3 test event...");
            deleteMessage(message);
            log.info("Deleted S3 test event.");
            return keys; // Exit early
        }

        for (JsonElement record : recordsArray) {
            JsonObject recordObject = (JsonObject) record;
            String objectKey = recordObject.getAsJsonObject("s3")
                    .getAsJsonObject("object")
                    .get("key").getAsString();
            keys.add(objectKey);
        }
        return keys;
    }

    private void deleteMessage(Message message) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest
                .builder()
                .queueUrl(SQS_QUEUE_URL)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }

    private long getUserIdFromKey(String resumeKey) {
        String userIdStr = resumeKey.split("@")[0];
        return Long.parseLong(userIdStr);
    }
}
