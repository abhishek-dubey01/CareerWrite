-- ============================================================
-- CareerWrite database setup + sample data
-- Run this whole file in MySQL Workbench (or `mysql -u root -p < careerwrite.sql`)
-- ============================================================

CREATE DATABASE IF NOT EXISTS careerwrite;
USE careerwrite;

-- NOTE: Spring Boot (Hibernate ddl-auto=update) will actually create these
-- tables for you automatically the first time you run the backend.
-- This script is provided so you can also set things up manually / understand
-- the schema, and to load sample data.

DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    salary VARCHAR(100),
    posted_date DATE,
    recruiter_id BIGINT NOT NULL,
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    application_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    UNIQUE KEY unique_application (job_id, user_id),
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    phone VARCHAR(20),
    skills TEXT,
    education TEXT,
    experience TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
-- SAMPLE DATA
-- Passwords below are BCrypt hashes of the plain text "password123"
-- so you can log in with any of these accounts immediately.
-- ============================================================

INSERT INTO users (name, email, password, role) VALUES
('Aditi Sharma', 'aditi.sharma@example.com', '$2b$10$33gT8CyD6.Wch0hlf0gsbe6nRuP.6BDTFVwYzVjvyvMlcfYlpTQn2', 'JOB_SEEKER'),
('Rohan Verma', 'rohan.verma@example.com', '$2b$10$33gT8CyD6.Wch0hlf0gsbe6nRuP.6BDTFVwYzVjvyvMlcfYlpTQn2', 'JOB_SEEKER'),
('Priya Nair', 'priya.nair@example.com', '$2b$10$33gT8CyD6.Wch0hlf0gsbe6nRuP.6BDTFVwYzVjvyvMlcfYlpTQn2', 'JOB_SEEKER'),
('Karan Mehta', 'karan.mehta@example.com', '$2b$10$33gT8CyD6.Wch0hlf0gsbe6nRuP.6BDTFVwYzVjvyvMlcfYlpTQn2', 'JOB_SEEKER'),
('TechNova Solutions HR', 'hr@technova.example.com', '$2b$10$33gT8CyD6.Wch0hlf0gsbe6nRuP.6BDTFVwYzVjvyvMlcfYlpTQn2', 'RECRUITER'),
('BrightWave IT Services HR', 'hr@brightwave.example.com', '$2b$10$33gT8CyD6.Wch0hlf0gsbe6nRuP.6BDTFVwYzVjvyvMlcfYlpTQn2', 'RECRUITER');

INSERT INTO jobs (title, company, location, category, description, salary, posted_date, recruiter_id) VALUES
('Java Backend Developer', 'TechNova Solutions', 'Indore', 'IT', 'Work on Spring Boot microservices and REST APIs for our product team.', '6-9 LPA', CURDATE(), 5),
('Frontend Developer (React)', 'TechNova Solutions', 'Pune', 'IT', 'Build and maintain React-based dashboards for enterprise clients.', '5-8 LPA', CURDATE(), 5),
('Full Stack Developer', 'BrightWave IT Services', 'Bangalore', 'IT', 'End-to-end feature development using Spring Boot and JavaScript.', '7-10 LPA', CURDATE(), 6),
('MySQL Database Administrator', 'BrightWave IT Services', 'Hyderabad', 'IT', 'Manage and optimize MySQL databases for production systems.', '6-8 LPA', CURDATE(), 6),
('QA / Manual Tester', 'TechNova Solutions', 'Noida', 'IT', 'Perform manual and basic automated testing for web applications.', '3.5-5 LPA', CURDATE(), 5),
('HR Executive', 'BrightWave IT Services', 'Bhopal', 'HR', 'Handle recruitment drives, onboarding and employee engagement.', '3-4.5 LPA', CURDATE(), 6),
('Digital Marketing Associate', 'TechNova Solutions', 'Indore', 'Marketing', 'Manage social media campaigns and SEO for company products.', '3-5 LPA', CURDATE(), 5),
('Business Analyst', 'BrightWave IT Services', 'Pune', 'Business', 'Gather requirements and prepare reports for client projects.', '5-7 LPA', CURDATE(), 6),
('Junior Java Developer', 'TechNova Solutions', 'Bangalore', 'IT', 'Entry-level role for freshers strong in core Java and OOP concepts.', '3-4 LPA', CURDATE(), 5),
('Technical Support Engineer', 'BrightWave IT Services', 'Noida', 'IT', 'Provide L1/L2 support for enterprise software customers.', '3-4.5 LPA', CURDATE(), 6);

INSERT INTO applications (job_id, user_id, application_date, status) VALUES
(1, 1, CURDATE(), 'PENDING'),
(1, 2, CURDATE(), 'SHORTLISTED'),
(3, 1, CURDATE(), 'PENDING'),
(9, 3, CURDATE(), 'PENDING'),
(9, 4, CURDATE(), 'REJECTED'),
(5, 2, CURDATE(), 'PENDING');

INSERT INTO profiles (user_id, phone, skills, education, experience) VALUES
(1, '9876500001', 'Java, Spring Boot, MySQL', 'B.Tech in Computer Science, SGSITS Indore', 'Fresher'),
(2, '9876500002', 'React, JavaScript, HTML, CSS', 'B.E. in IT, Pune University', '1 year internship at a startup'),
(3, '9876500003', 'Python, Data Analysis, SQL', 'B.Sc. Computer Science, Bangalore', 'Fresher'),
(4, '9876500004', 'Manual Testing, Selenium basics', 'B.Tech in ECE, Noida', 'Fresher');
