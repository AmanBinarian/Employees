package com.aman.employees.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
//import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class SqsListenerService {

    private static final Logger logger = LoggerFactory.getLogger(SqsListenerService.class);

    private final SqsClient sqsClient;

    @Value("${cloud.aws.sqs.queue-url}")
    private String SQS_QUEUE_URL;

    public SqsListenerService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public List<String> pollMessages() {
        List<String> messagesList = new ArrayList<>();

        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(SQS_QUEUE_URL)
                .maxNumberOfMessages(1) 
                .waitTimeSeconds(5)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {
            messagesList.add(message.body());
            
            logger.info("Received SQS Message: {}", message.body());

//            // Delete the message after processing
//            sqsClient.deleteMessage(DeleteMessageRequest.builder()
//                    .queueUrl(SQS_QUEUE_URL)
//                    .receiptHandle(message.receiptHandle())
//                    .build());
//
//            logger.info("Deleted SQS Message with receipt handle: {}", message.receiptHandle());
        }

        return messagesList;
    }
}
