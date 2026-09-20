package com.careerwrite.controller;

import com.careerwrite.entity.Job;
import com.careerwrite.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    // GET /api/jobs/search?keyword=java&location=pune&category=IT
    @GetMapping("/search")
    public List<Job> searchJobs(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) String location,
                                 @RequestParam(required = false) String category) {
        return jobService.searchJobs(keyword, location, category);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public List<Job> getJobsByRecruiter(@PathVariable Long recruiterId) {
        return jobService.getJobsByRecruiter(recruiterId);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job saved = jobService.createJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
        return jobService.updateJob(id, job);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
