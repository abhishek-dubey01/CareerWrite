# CareerWrite – Online Job Portal

A simple full-stack job portal built as a college mini-project by a small student team.
Job seekers can browse/search/apply for jobs; recruiters can post jobs and view applications.

---

## 1. Project Overview

CareerWrite connects **job seekers** and **recruiters** on one platform:

- Job seekers register, browse/search jobs, apply, and track their applications.
- Recruiters register, post jobs, and see who applied.

It's intentionally kept simple — no microservices, no JWT, no frontend framework — so every
part of it is easy to explain and defend in a viva/interview.

## 2. Features

**Job Seeker**
- Register / Login / Logout
- Browse all jobs, search by title/company, filter by location/category
- View job details and apply
- View "My Applications" with status
- Maintain a simple profile (phone, skills, education, experience)
- Dashboard with total jobs, jobs applied, recent applications, latest jobs

**Recruiter**
- Register / Login / Logout
- Post a new job
- View jobs they've posted
- View applications received for their jobs
- Dashboard with jobs posted, total applications, recent applications

## 3. Technology Stack

| Layer      | Technology |
|------------|------------|
| Frontend   | HTML5, CSS3, Vanilla JavaScript (fetch API) |
| Backend    | Java 21, Spring Boot 3, Spring Web, Spring Data JPA, Hibernate |
| Database   | MySQL |
| Build tool | Maven |
| Tools      | IntelliJ IDEA / VS Code, MySQL Workbench, Postman, Git |

## 4. Folder Structure

```
CareerWrite/
├── backend/                        Spring Boot project (Maven)
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/careerwrite/
│       │   ├── CareerWriteApplication.java
│       │   ├── controller/         REST endpoints (AuthController, JobController, ...)
│       │   ├── service/            Business logic
│       │   ├── repository/         Spring Data JPA interfaces
│       │   ├── entity/             JPA entities = DB tables
│       │   ├── dto/                Request/response objects
│       │   └── config/             CORS config, global exception handler
│       └── resources/application.properties
│
├── frontend/                       Plain HTML/CSS/JS, no build step
│   ├── index.html, login.html, register.html, jobs.html,
│   │   job-details.html, dashboard.html, profile.html,
│   │   post-job.html, applications.html
│   ├── css/style.css
│   └── js/ (api.js, auth.js, jobs.js, dashboard.js, profile.js)
│
├── database/careerwrite.sql        Schema + sample data
└── README.md
```

## 5. Database Setup

1. Open **MySQL Workbench** (or the `mysql` CLI) and connect to your local MySQL server.
2. Run the script in `database/careerwrite.sql`. It will:
   - Create the `careerwrite` database
   - Create `users`, `jobs`, `applications`, `profiles` tables
   - Insert sample jobs, users and applications

```bash
mysql -u root -p < database/careerwrite.sql
```

> Note: Spring Boot is also configured with `spring.jpa.hibernate.ddl-auto=update`, so if you
> skip the script and just create an empty `careerwrite` database, Hibernate will auto-create
> the tables the first time you run the backend. You'd just be missing the sample data.

## 6. MySQL Commands Cheat-Sheet

```sql
CREATE DATABASE careerwrite;
USE careerwrite;
SHOW TABLES;
SELECT * FROM users;
SELECT * FROM jobs;
SELECT * FROM applications;
```

## 7. Configure `application.properties`

Open `backend/src/main/resources/application.properties` and put **your own MySQL password**
on this line:

```properties
spring.datasource.password=YOUR_PASSWORD
```

Everything else (URL, username, JPA settings) can stay as-is if you're running MySQL locally
on the default port 3306 with the default `root` user.

## 8. How to Run the Backend (Spring Boot)

**Using IntelliJ IDEA:**
1. Open the `backend` folder as a Maven project.
2. Let IntelliJ download dependencies.
3. Set your MySQL password in `application.properties` (step 7).
4. Run `CareerWriteApplication.java`.
5. Backend starts at `http://localhost:8080`.

**Using terminal:**
```bash
cd backend
mvn spring-boot:run
```

Test it's alive: `GET http://localhost:8080/api/jobs` should return a JSON array of jobs.

## 9. How to Run the Frontend

The frontend is plain static HTML/CSS/JS — no build step needed.

- Easiest: open `frontend/index.html` directly in your browser, **or**
- Recommended: use a simple local server (e.g. VS Code's "Live Server" extension) so relative
  paths and fetch requests behave consistently — right-click `index.html` → "Open with Live Server".

Make sure the backend (step 8) is already running on port 8080, since the frontend calls
`http://localhost:8080/api/...` (see `frontend/js/api.js`).

## 10. API List

| Method | Endpoint | Description |
|--------|----------|--------------|
| POST | `/api/auth/register` | Register a new user (job seeker or recruiter) |
| POST | `/api/auth/login` | Login and get user info |
| POST | `/api/auth/logout` | Stateless logout (no-op, for completeness) |
| GET | `/api/jobs` | Get all jobs |
| GET | `/api/jobs/{id}` | Get one job by id |
| GET | `/api/jobs/search?keyword=&location=&category=` | Search/filter jobs |
| GET | `/api/jobs/recruiter/{recruiterId}` | Jobs posted by a recruiter |
| POST | `/api/jobs` | Create a job (recruiter) |
| PUT | `/api/jobs/{id}` | Update a job |
| DELETE | `/api/jobs/{id}` | Delete a job |
| POST | `/api/applications` | Apply for a job — body: `{ "jobId": 1, "userId": 2 }` |
| GET | `/api/applications/user/{userId}` | Applications submitted by a job seeker |
| GET | `/api/applications/job/{jobId}` | Applications received for a job |
| GET | `/api/profile/{userId}` | Get a user's profile |
| PUT | `/api/profile/{userId}` | Create/update a user's profile |

## 11. Sample Login Credentials

All sample accounts use the password: **`password123`**

| Role | Email |
|------|-------|
| Job Seeker | aditi.sharma@example.com |
| Job Seeker | rohan.verma@example.com |
| Job Seeker | priya.nair@example.com |
| Job Seeker | karan.mehta@example.com |
| Recruiter | hr@technova.example.com |
| Recruiter | hr@brightwave.example.com |

## 12. Screenshots

_(Add screenshots of the homepage, jobs page, job details, dashboard, and post-job page here
once you run the project.)_

- Homepage: `screenshots/home.png`
- Jobs listing: `screenshots/jobs.png`
- Job details: `screenshots/job-details.png`
- Dashboard: `screenshots/dashboard.png`

## 13. Future Improvements

- Resume upload (file storage) for job seeker applications
- Email notifications when an application status changes
- Pagination for the jobs list
- Admin panel to moderate job postings
- Proper session-based or JWT authentication
- Recruiter can update application status (Shortlisted/Rejected) from the UI

---

## Interview-Friendly Explanations

### What problem does CareerWrite solve?
It gives job seekers one place to find and apply for jobs, and gives recruiters one place to
post jobs and review who applied — replacing scattered emails/spreadsheets with a small,
structured web app.

### Why Spring Boot?
Spring Boot removes most manual configuration (embedded Tomcat, auto-configured beans) so we
can focus on writing REST endpoints and business logic quickly, using an
industry-standard Java framework.

### Why MySQL?
It's a free, widely used relational database that fits our data well — users, jobs and
applications are naturally related tables with clear foreign keys.

### How does the frontend communicate with the backend?
The frontend's JavaScript uses the built-in `fetch()` API to send HTTP requests (GET/POST/PUT/
DELETE) with JSON bodies to the Spring Boot REST endpoints, and reads the JSON responses to
update the page. `frontend/js/api.js` centralizes this logic.

### What does REST API mean in this project?
Each URL represents a *resource* (`/api/jobs`, `/api/applications`) and the HTTP method
(GET/POST/PUT/DELETE) describes the action on that resource. Responses are JSON, not HTML.

### What do JPA/Hibernate do?
JPA is a specification for mapping Java objects to database tables; Hibernate is the
implementation Spring Boot uses. Our `@Entity` classes (e.g. `Job.java`) map directly to
tables, so we write Java code instead of manual SQL for most operations.

### How does registration work?
Frontend sends `{ name, email, password, role }` to `POST /api/auth/register`. The backend
checks the email isn't already used, hashes the password with BCrypt, saves a new `User` row,
and returns the user's id/name/email/role (never the password) to the frontend.

### How does login work?
Frontend sends `{ email, password }` to `POST /api/auth/login`. The backend looks up the user
by email, checks the given password against the stored BCrypt hash using
`passwordEncoder.matches()`, and if it matches, returns the same user info. The frontend saves
this in `localStorage` to remember who's logged in.

### How does job posting work?
A logged-in recruiter fills the post-job form; the frontend sends the job fields plus
`recruiterId` to `POST /api/jobs`. The backend verifies that user is really a `RECRUITER`,
sets `postedDate` to today, and saves the job.

### How does job application work?
A logged-in job seeker clicks "Apply" on a job details page; the frontend sends
`{ jobId, userId }` to `POST /api/applications`. The backend checks the user is a
`JOB_SEEKER`, checks they haven't already applied to that job (unique constraint on
`job_id + user_id`), and creates an application row with status `PENDING`.

### Explain the database relationships
- One user can post many jobs (`users 1—M jobs`, via `jobs.recruiter_id`)
- One user can submit many applications (`users 1—M applications`, via `applications.user_id`)
- One job can receive many applications (`jobs 1—M applications`, via `applications.job_id`)
- One user has one profile (`users 1—1 profiles`, via `profiles.user_id`)

### Explain the Controller → Service → Repository flow
1. **Controller** receives the HTTP request, does light validation (`@Valid`), and calls a
   service method — it contains no business logic.
2. **Service** contains the actual business rules (e.g. "can't apply twice", "only recruiters
   can post jobs") and talks to one or more repositories.
3. **Repository** is a Spring Data JPA interface that talks directly to the database (no SQL
   written by us for basic operations).

This separation keeps each layer focused on one job and makes the code easier to test and
explain.

### 20 Likely Interview Questions & Simple Answers

1. **What is Spring Boot?**
   A framework that simplifies building Java web applications by auto-configuring common
   components (server, JPA, JSON handling) so you write less boilerplate.

2. **What is a REST API?**
   An API that exposes resources via URLs and standard HTTP methods (GET/POST/PUT/DELETE),
   returning data (usually JSON) instead of full HTML pages.

3. **What is JPA vs Hibernate?**
   JPA is the specification/interface; Hibernate is the most popular implementation of that
   specification, and it's what actually runs the SQL behind the scenes.

4. **What is `@Entity`?**
   An annotation marking a Java class as mapped to a database table.

5. **What is `@Repository`(here `JpaRepository`)?**
   An interface Spring Data JPA implements automatically at runtime, giving you CRUD methods
   without writing SQL.

6. **What is `@Service`?**
   An annotation marking a class that holds business logic, so Spring can manage it as a bean
   and inject it wherever needed.

7. **What is `@RestController`?**
   A class that handles incoming HTTP requests and returns data (usually JSON) directly in the
   response body, instead of rendering an HTML view.

8. **Why did you hash passwords instead of storing them as plain text?**
   Storing plain text passwords is a serious security risk if the database is ever
   compromised. BCrypt hashing means even we can't see the original password.

9. **How do you prevent a user from applying to the same job twice?**
   A unique database constraint on `(job_id, user_id)` in the `applications` table, plus an
   explicit check in `ApplicationService` before saving.

10. **How does the frontend know who is logged in?**
    The backend returns user info (id, name, email, role) on login/register, and the frontend
    stores it in the browser's `localStorage` so it persists across page loads.

11. **Why didn't you use Spring Security/JWT?**
    To keep the authentication flow simple and easy to explain for a college project; the app
    already achieves password hashing and role checks without the added complexity.

12. **What is CORS, and why did you configure it?**
    Browsers block frontend JavaScript from calling an API on a different origin by default.
    `WebConfig` explicitly allows the frontend to call our backend's `/api/**` endpoints.

13. **What is `ddl-auto=update`?**
    A Hibernate setting that automatically creates/updates database tables to match your
    `@Entity` classes, so you don't have to write `CREATE TABLE` statements by hand during
    development.

14. **What is a DTO and why use one?**
    A Data Transfer Object is a plain class used only to carry data between frontend and
    backend (e.g. `RegisterRequest`), keeping it separate from the `@Entity` used for the
    database.

15. **How is error handling done in this project?**
    A `GlobalExceptionHandler` (`@RestControllerAdvice`) catches exceptions thrown anywhere in
    the app and converts them into a consistent JSON error response.

16. **What's the difference between `GET`, `POST`, `PUT`, and `DELETE`?**
    GET retrieves data, POST creates new data, PUT updates existing data, and DELETE removes
    data.

17. **Why is `recruiterId` stored on `Job` instead of the whole `User` object?**
    It keeps the JSON responses simple (just an id) rather than nesting a full user object,
    which is easier for a beginner-level frontend to handle.

18. **What does `@Valid` do on a controller method?**
    It triggers Bean Validation on the request body (e.g. checking `@NotBlank`, `@Email`)
    before the method body runs, and throws an exception automatically if validation fails.

19. **How would you scale this project further?**
    Add pagination, caching, proper authentication (JWT/sessions), containerize with Docker,
    and split the frontend into a framework like React if the UI grows more complex.

20. **What was the hardest part of building this project?**
    (Answer this one from your own experience — e.g. keeping the frontend fetch URLs in sync
    with backend endpoints, or getting the database relationships right.)
