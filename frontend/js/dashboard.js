// ===================================================
// dashboard.js
// Powers dashboard.html - shows different stats depending on role.
// ===================================================

async function loadDashboard() {
    const wrapper = document.getElementById("dashboard-wrapper");
    if (!wrapper) return;

    const user = requireLogin();
    if (!user) return;

    document.getElementById("welcome-name").textContent = user.name;

    if (user.role === "JOB_SEEKER") {
        await loadJobSeekerDashboard(user);
    } else {
        await loadRecruiterDashboard(user);
    }
}

async function loadJobSeekerDashboard(user) {
    document.getElementById("dashboard-role-label").textContent = "Job Seeker Dashboard";
    document.getElementById("label-total-jobs").textContent = "Total Jobs Available";
    document.getElementById("label-applied").textContent = "Jobs Applied";

    try {
        const [allJobs, myApplications] = await Promise.all([
            apiGet("/jobs"),
            apiGet("/applications/user/" + user.userId)
        ]);

        document.getElementById("stat-total-jobs").textContent = allJobs.length;
        document.getElementById("stat-applied").textContent = myApplications.length;

        const jobLookup = {};
        allJobs.forEach(j => jobLookup[j.id] = j);

        const recentApplicationsHtml = myApplications.slice(-5).reverse().map(app => {
            const job = jobLookup[app.jobId] || {};
            return `<tr>
                <td>${job.title || "Job removed"}</td>
                <td>${job.company || "-"}</td>
                <td><span class="status status-${app.status}">${app.status}</span></td>
            </tr>`;
        }).join("");

        document.getElementById("recent-applications-body").innerHTML =
            recentApplicationsHtml || `<tr><td colspan="3" class="empty-msg">No applications yet.</td></tr>`;

        const latestJobsHtml = allJobs.slice(-6).reverse().map(jobCardHtml).join("");
        document.getElementById("latest-jobs-grid").innerHTML =
            latestJobsHtml || `<p class="empty-msg">No jobs posted yet.</p>`;

    } catch (err) {
        wrapper.innerHTML += `<p class="empty-msg">${err.message}</p>`;
    }
}

async function loadRecruiterDashboard(user) {
    document.getElementById("dashboard-role-label").textContent = "Recruiter Dashboard";
    document.getElementById("recruiter-extra").style.display = "block";
    document.getElementById("seeker-extra").style.display = "none";
    document.getElementById("label-total-jobs").textContent = "Jobs Posted";
    document.getElementById("label-applied").textContent = "Total Applications";

    try {
        const myJobs = await apiGet("/jobs/recruiter/" + user.userId);
        document.getElementById("stat-total-jobs").textContent = myJobs.length;

        const applicationsArrays = await Promise.all(
            myJobs.map(j => apiGet("/applications/job/" + j.id))
        );
        const allApplications = applicationsArrays.flat();
        document.getElementById("stat-applied").textContent = allApplications.length;

        const jobLookup = {};
        myJobs.forEach(j => jobLookup[j.id] = j);

        const recentHtml = allApplications.slice(-5).reverse().map(app => {
            const job = jobLookup[app.jobId] || {};
            return `<tr>
                <td>${job.title || "-"}</td>
                <td>Applicant #${app.userId}</td>
                <td><span class="status status-${app.status}">${app.status}</span></td>
            </tr>`;
        }).join("");

        document.getElementById("recent-applications-body").innerHTML =
            recentHtml || `<tr><td colspan="3" class="empty-msg">No applications received yet.</td></tr>`;

    } catch (err) {
        wrapper.innerHTML += `<p class="empty-msg">${err.message}</p>`;
    }
}

document.addEventListener("DOMContentLoaded", loadDashboard);
