package com.careerwrite.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

// Maps to the "jobs" table. Each job is posted by one recruiter (a User
// whose role is RECRUITER). We keep just the recruiter's id as a foreign
// key (recruiter_id) instead of loading the whole User object every time,
// which keeps the JSON responses simple for a beginner project.
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String category;

    @Column(length = 2000)
    private String description;

    private String salary;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    @Column(name = "recruiter_id", nullable = false)
    private Long recruiterId;

    public Job() {
    }

    public Job(String title, String company, String location, String category,
                String description, String salary, LocalDate postedDate, Long recruiterId) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.category = category;
        this.description = description;
        this.salary = salary;
        this.postedDate = postedDate;
        this.recruiterId = recruiterId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }
}
