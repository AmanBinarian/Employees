package com.aman.employees.service;

import com.aman.employees.model.Employee;
import com.aman.employees.repository.EmployeeRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SqsListenerService sqsListenerService;

    @Value("${cloud.aws.sqs.queue-url}")
    private String SQS_QUEUE_URL;

    @Transactional(rollbackFor = {Exception.class})
    public Employee createOrUpdateEmployee(Employee employee) {
        try {
            // Save employee
            employeeRepository.save(employee);

            // Send message to SQS
            String messageId = sendMessageToSQS("Employee inserted/updated with details: " +
                    "Employee Id : " + employee.getId() + ", " + 
                    "Employee Name : " + employee.getUsername() + ", " + 
                    "Employee Role : " + employee.getRole() + ", " + 
                    "Employee Department : " + employee.getDepartment());

            // Generate and upload PDF
            if (employee.getId() != null) {
                String pdfUrl = generatePdfAndUpload(employee, messageId);
                logger.info("EmployeeService : createOrUpdateEmployee : PDF uploaded successfully Pdf Url : {}", pdfUrl);
            }

            List<String> latestMessages = sqsListenerService.pollMessages();
            
        
            for (int i = 1; i < 11; i++) {
                logger.info("SQS Message [{}]: {}", i, latestMessages.get(0));
            }

            if (!latestMessages.isEmpty()) {
                employee.setMessage(latestMessages.get(0));
            }

            return employee;
        } catch (Exception e) {
            logger.error("Error in createOrUpdateEmployee: {}", e.getMessage(), e);
            throw new RuntimeException("Error in createOrUpdateEmployee: " + e.getMessage(), e);
        }
    }

    private String sendMessageToSQS(String messageBody) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(SQS_QUEUE_URL)
                .messageBody(messageBody)
                .build();
        SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);
        return response.messageId();
    }

    private String generatePdfAndUpload(Employee employee, String messageId) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            generatePdf(employee, messageId, outputStream);
            return s3Service.uploadFile(outputStream.toByteArray(), employee.getId() + ".pdf");
        } catch (Exception e) {
            logger.error("Error while generating and uploading PDF", e);
            throw new RuntimeException("Error while generating and uploading PDF", e);
        }
    }

    private void generatePdf(Employee employee, String messageId, OutputStream outputStream) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph("Employee ID : " + employee.getId()));
        document.add(new Paragraph("Username : " + employee.getUsername()));
        document.add(new Paragraph("Password : " + employee.getPassword()));
        document.add(new Paragraph("Role : " + employee.getRole()));
        document.add(new Paragraph("Department : " + employee.getDepartment()));
        document.add(new Paragraph("SQS Message ID : " + messageId));
        document.close();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeeDetails() {
        return employeeRepository.();
    }
}
