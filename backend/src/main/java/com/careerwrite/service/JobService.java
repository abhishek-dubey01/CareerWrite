package com.careerwrite.service;

import com.careerwrite.dto.ApiException;
import com.careerwrite.entity.Job;
import com.careerwrite.entity.User;
import com.careerwrite.repository.JobRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserService userService;

    public JobService(JobRepository jobRepository, UserService userService) {
        this.jobRepository = jobRepository;
        this.userService = userService;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ApiException("Job not found", HttpStatus.NOT_FOUND));
    }

    public List<Job> searchJobs(String keyword, String location, String category) {
        return jobRepository.searchJobs(keyword, location, category);
    }

    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }

    public Job createJob(Job job) {
        // Make sure the recruiterId given actually belongs to a RECRUITER user
        User recruiter = userService.getUserOrThrow(job.getRecruiterId());
        if (!recruiter.getRole().equals("RECRUITER")) {
            throw new ApiException("Only recruiters can post jobs", HttpStatus.FORBIDDEN);
        }
        job.setPostedDate(LocalDate.now());
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job updatedJob) {
        Job job = getJobById(id);
        job.setTitle(updatedJob.getTitle());
        job.setCompany(updatedJob.getCompany());
        job.setLocation(updatedJob.getLocation());
        job.setCategory(updatedJob.getCategory());
        job.setDescription(updatedJob.getDescription());
        job.setSalary(updatedJob.getSalary());
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        Job job = getJobById(id);
        jobRepository.delete(job);
    }
}
