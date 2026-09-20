// ===================================================
// profile.js
// Powers profile.html - loads and saves the job seeker's profile.
// (Recruiters just see their name/email/role, no extra profile fields.)
// ===================================================

async function loadProfilePage() {
    const form = document.getElementById("profile-form");
    if (!form) return;

    const user = requireLogin();
    if (!user) return;

    document.getElementById("profile-name").textContent = user.name;
    document.getElementById("profile-email").textContent = user.email;
    document.getElementById("profile-role").textContent = user.role.replace("_", " ");

    if (user.role === "RECRUITER") {
        document.getElementById("job-seeker-fields").style.display = "none";
        return;
    }

    try {
        const profile = await apiGet("/profile/" + user.userId);
        document.getElementById("profile-phone").value = profile.phone || "";
        document.getElementById("profile-skills").value = profile.skills || "";
        document.getElementById("profile-education").value = profile.education || "";
        document.getElementById("profile-experience").value = profile.experience || "";
    } catch (err) {
        showError(err.message);
    }

    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        hideMessages();

        const body = {
            phone: document.getElementById("profile-phone").value.trim(),
            skills: document.getElementById("profile-skills").value.trim(),
            education: document.getElementById("profile-education").value.trim(),
            experience: document.getElementById("profile-experience").value.trim()
        };

        try {
            await apiPut("/profile/" + user.userId, body);
            showSuccess("Profile updated successfully.");
        } catch (err) {
            showError(err.message);
        }
    });
}

document.addEventListener("DOMContentLoaded", loadProfilePage);
