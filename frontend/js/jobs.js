// ===================================================
// jobs.js
// Powers jobs.html (list + search), job-details.html (view + apply),
// post-job.html (recruiter posts a job) and applications.html.
// ===================================================

function jobCardHtml(job) {
    return `
        <div class="job-card">
            <h3>${escapeHtml(job.title)}</h3>
            <div class="company">${escapeHtml(job.company)}</div>
            <div class="meta">${escapeHtml(job.location)} &middot; ${escapeHtml(job.salary || "Not disclosed")}</div>
            <span class="tag">${escapeHtml(job.category)}</span>
            <div class="footer-row">
                <span class="meta">Posted: ${job.postedDate || "-"}</span>
                <a class="btn btn-small" href="job-details.html?id=${job.id}">View Details</a>
            </div>
        </div>
    `;
}

function escapeHtml(text) {
    if (!text) return "";
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

// ---------- jobs.html ----------
async function loadJobsPage() {
    const grid = document.getElementById("job-grid");
    if (!grid) return;

    async function runSearch() {
        const keyword = document.getElementById("search-keyword").value.trim();
        const location = document.getElementById("search-location").value.trim();
        const category = document.getElementById("search-category").value;

        grid.innerHTML = `<p class="empty-msg">Loading jobs...</p>`;

        try {
            const params = new URLSearchParams();
            if (keyword) params.set("keyword", keyword);
            if (location) params.set("location", location);
            if (category) params.set("category", category);

            const jobs = await apiGet("/jobs/search?" + params.toString());
            renderJobs(jobs);
        } catch (err) {
            grid.innerHTML = `<p class="empty-msg">Could not load jobs: ${escapeHtml(err.message)}</p>`;
        }
    }

    function renderJobs(jobs) {
        if (!jobs || jobs.length === 0) {
            grid.innerHTML = `<p class="empty-msg">No jobs found matching your search.</p>`;
            return;
        }
        grid.innerHTML = jobs.map(jobCardHtml).join("");
    }

    document.getElementById("search-btn").addEventListener("click", runSearch);
    document.getElementById("search-keyword").addEventListener("keyup", function (e) {
        if (e.key === "Enter") runSearch();
    });

    runSearch();
}

// ---------- job-details.html ----------
async function loadJobDetailsPage() {
    const container = document.getElementById("job-detail-container");
    if (!container) return;

    const jobId = new URLSearchParams(window.location.search).get("id");
    if (!jobId) {
        container.innerHTML = `<p class="empty-msg">No job selected.</p>`;
        return;
    }

    try {
        const job = await apiGet("/jobs/" + jobId);

        const user = getLoggedInUser();
        let applyButtonHtml = `<a class="btn" href="login.html">Login to Apply</a>`;

        if (user && user.role === "JOB_SEEKER") {
            applyButtonHtml = `<button class="btn" id="apply-btn">Apply for this Job</button>`;
        } else if (user && user.role === "RECRUITER") {
            applyButtonHtml = "";
        }

        container.innerHTML = `
            <div class="job-detail-card">
                <h2>${escapeHtml(job.title)}</h2>
                <div class="company">${escapeHtml(job.company)}</div>
                <div class="meta-row">
                    ${escapeHtml(job.location)} &middot; ${escapeHtml(job.category)} &middot; ${escapeHtml(job.salary || "Salary not disclosed")}
                </div>
                <div class="description">${escapeHtml(job.description || "No description provided.")}</div>
                <p id="apply-msg" class="success-msg"></p>
                ${applyButtonHtml}
            </div>
        `;

        const applyBtn = document.getElementById("apply-btn");
        if (applyBtn) {
            applyBtn.addEventListener("click", async function () {
                try {
                    await apiPost("/applications", { jobId: Number(jobId), userId: user.userId });
                    applyBtn.disabled = true;
                    applyBtn.textContent = "Applied";
                    const msg = document.getElementById("apply-msg");
                    msg.textContent = "You have successfully applied for this job.";
                    msg.style.display = "block";
                } catch (err) {
                    alert(err.message);
                }
            });
        }
    } catch (err) {
        container.innerHTML = `<p class="empty-msg">${escapeHtml(err.message)}</p>`;
    }
}

// ---------- post-job.html ----------
function initPostJobForm() {
    const form = document.getElementById("post-job-form");
    if (!form) return;

    const user = requireLogin();
    if (!user) return;

    if (user.role !== "RECRUITER") {
        document.getElementById("post-job-wrapper").innerHTML =
            `<p class="empty-msg">Only recruiters can post jobs.</p>`;
        return;
    }

    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        hideMessages();

        const job = {
            title: document.getElementById("job-title").value.trim(),
            company: document.getElementById("job-company").value.trim(),
            location: document.getElementById("job-location").value.trim(),
            category: document.getElementById("job-category").value.trim(),
            salary: document.getElementById("job-salary").value.trim(),
            description: document.getElementById("job-description").value.trim(),
            recruiterId: user.userId
        };

        try {
            await apiPost("/jobs", job);
            showSuccess("Job posted successfully!");
            form.reset();
        } catch (err) {
            showError(err.message);
        }
    });
}

// ---------- applications.html ----------
async function loadApplicationsPage() {
    const tableBody = document.getElementById("applications-body");
    if (!tableBody) return;

    const user = requireLogin();
    if (!user) return;

    try {
        let applications;
        let jobLookup = {};

        if (user.role === "JOB_SEEKER") {
            applications = await apiGet("/applications/user/" + user.userId);
            const allJobs = await apiGet("/jobs");
            allJobs.forEach(j => jobLookup[j.id] = j);

            if (applications.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="4" class="empty-msg">You haven't applied to any jobs yet.</td></tr>`;
                return;
            }

            tableBody.innerHTML = applications.map(app => {
                const job = jobLookup[app.jobId] || {};
                return `
                    <tr>
                        <td>${escapeHtml(job.title || "Job removed")}</td>
                        <td>${escapeHtml(job.company || "-")}</td>
                        <td>${app.applicationDate || "-"}</td>
                        <td><span class="status status-${app.status}">${app.status}</span></td>
                    </tr>
                `;
            }).join("");

        } else {
            // RECRUITER: show applications received across all their jobs
            const myJobs = await apiGet("/jobs/recruiter/" + user.userId);
            myJobs.forEach(j => jobLookup[j.id] = j);

            if (myJobs.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="4" class="empty-msg">You haven't posted any jobs yet.</td></tr>`;
                return;
            }

            const allApplicationsArrays = await Promise.all(
                myJobs.map(j => apiGet("/applications/job/" + j.id))
            );
            const allApplications = allApplicationsArrays.flat();

            if (allApplications.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="4" class="empty-msg">No applications received yet.</td></tr>`;
                return;
            }

            tableBody.innerHTML = allApplications.map(app => {
                const job = jobLookup[app.jobId] || {};
                return `
                    <tr>
                        <td>${escapeHtml(job.title || "-")}</td>
                        <td>Applicant #${app.userId}</td>
                        <td>${app.applicationDate || "-"}</td>
                        <td><span class="status status-${app.status}">${app.status}</span></td>
                    </tr>
                `;
            }).join("");
        }
    } catch (err) {
        tableBody.innerHTML = `<tr><td colspan="4" class="empty-msg">${escapeHtml(err.message)}</td></tr>`;
    }
}

document.addEventListener("DOMContentLoaded", function () {
    loadJobsPage();
    loadJobDetailsPage();
    initPostJobForm();
    loadApplicationsPage();
});
