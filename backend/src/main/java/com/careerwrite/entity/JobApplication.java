package com.careerwrite.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

// Maps to the "applications" table. Represents one job seeker applying to
// one job. Named "JobApplication" instead of "Application" in Java because
// "Application" clashes with Spring's own classes - the table itself is
// still called "applications".
@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "user_id"}))
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    // PENDING, SHORTLISTED, REJECTED
    @Column(nullable = false)
    private String status;

    public JobApplication() {
    }

    public JobApplication(Long jobId, Long userId, LocalDate applicationDate, String status) {
        this.jobId = jobId;
        this.userId = userId;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
