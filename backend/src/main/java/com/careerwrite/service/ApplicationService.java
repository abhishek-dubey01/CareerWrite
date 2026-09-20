package com.careerwrite.service;

import com.careerwrite.dto.ApiException;
import com.careerwrite.entity.Job;
import com.careerwrite.entity.JobApplication;
import com.careerwrite.entity.User;
import com.careerwrite.repository.JobApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobService jobService;
    private final UserService userService;

    public ApplicationService(JobApplicationRepository applicationRepository,
                               JobService jobService,
                               UserService userService) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
        this.userService = userService;
    }

    public JobApplication apply(Long jobId, Long userId) {
        Job job = jobService.getJobById(jobId);
        User user = userService.getUserOrThrow(userId);

        if (!user.getRole().equals("JOB_SEEKER")) {
            throw new ApiException("Only job seekers can apply for jobs", HttpStatus.FORBIDDEN);
        }

        applicationRepository.findByJobIdAndUserId(jobId, userId).ifPresent(a -> {
            throw new ApiException("You have already applied for this job", HttpStatus.BAD_REQUEST);
        });

        JobApplication application = new JobApplication(job.getId(), user.getId(), LocalDate.now(), "PENDING");
        return applicationRepository.save(application);
    }

    public List<JobApplication> getApplicationsByUser(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    public List<JobApplication> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}
