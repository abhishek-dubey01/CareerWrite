package com.careerwrite.controller;

import com.careerwrite.entity.JobApplication;
import com.careerwrite.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Body: { "jobId": 3, "userId": 7 }
    @PostMapping
    public ResponseEntity<JobApplication> apply(@RequestBody Map<String, Long> body) {
        JobApplication application = applicationService.apply(body.get("jobId"), body.get("userId"));
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/user/{userId}")
    public List<JobApplication> getApplicationsByUser(@PathVariable Long userId) {
        return applicationService.getApplicationsByUser(userId);
    }

    @GetMapping("/job/{jobId}")
    public List<JobApplication> getApplicationsByJob(@PathVariable Long jobId) {
        return applicationService.getApplicationsByJob(jobId);
    }
}
