package com.aman.employees.controller;

import com.aman.employees.service.CodacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codacy")
@CrossOrigin(origins = "http://localhost:4200")
public class CodacyController {

    @Autowired
    private CodacyService codacyService;

    // Fetch all project issues (POST)
    @GetMapping("/issues")
    public ResponseEntity<String> getCodacyIssues() {
        return ResponseEntity.ok(codacyService.getProjectIssues());
    }

    // Fetch a single project issue (GET) by passing resultId as a query parameter
    @GetMapping("/issues/single")
    public ResponseEntity<String> getSingleCodacyIssue(@RequestParam String resultId) {
        return ResponseEntity.ok(codacyService.getSingleProjectIssue(resultId));
    }
}
