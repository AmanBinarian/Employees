package com.aman.employees.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CodacyService {

    private static final Logger logger = LoggerFactory.getLogger(CodacyService.class);

    @Value("${codacy.api.token}")
    private String codacyApiToken;

    @Value("${codacy.api.url}")
    private String codacyApiUrl;

    @Value("${codacy.project.id}")
    private String codacyProjectId;
    
    @Value("${codacy.organisationname}")
    private String organisation;

    private final RestTemplate restTemplate = new RestTemplate();

    // Fetch all project issues
    public String getProjectIssues() {
        String url = codacyApiUrl + "/analysis/organizations/gh/" + organisation + "/repositories/" + codacyProjectId + "/issues/search";
        return fetchCodacyData(url, "project issues", HttpMethod.POST);
    }

    // Fetch a single project issue using resultId
    public String getSingleProjectIssue(String resultId) {
        String url = codacyApiUrl + "/analysis/organizations/gh/" + organisation + "/repositories/" + codacyProjectId + "/issues/" + resultId;
        return fetchCodacyData(url, "single project issue", HttpMethod.GET);
    }

    // Common method to handle API calls
    private String fetchCodacyData(String url, String dataType, HttpMethod method) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching Codacy {}: {}", dataType, e.getMessage());
            return "Failed to fetch Codacy " + dataType + ": " + e.getMessage();
        }
    }

    // Create headers with authorization token
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + codacyApiToken);
        return headers;
    }
}
