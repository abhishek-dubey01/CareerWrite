// ===================================================
// auth.js
// Handles register/login form submission and the simple
// "who is logged in" state, stored in localStorage.
// ===================================================

const STORAGE_KEY = "careerwrite_user";

function saveLoggedInUser(user) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
}

function getLoggedInUser() {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
}

function clearLoggedInUser() {
    localStorage.removeItem(STORAGE_KEY);
}

function requireLogin() {
    const user = getLoggedInUser();
    if (!user) {
        window.location.href = "login.html";
        return null;
    }
    return user;
}

function logout() {
    clearLoggedInUser();
    window.location.href = "index.html";
}

// Updates the navbar links based on login state (used on every page)
function renderNavbar() {
    const navLinks = document.getElementById("nav-links");
    if (!navLinks) return;

    const user = getLoggedInUser();

    if (!user) {
        navLinks.innerHTML = `
            <li><a href="index.html">Home</a></li>
            <li><a href="jobs.html">Browse Jobs</a></li>
            <li><a href="login.html">Login</a></li>
            <li><a href="register.html">Register</a></li>
        `;
        return;
    }

    let links = `<li><a href="index.html">Home</a></li><li><a href="jobs.html">Browse Jobs</a></li>`;

    if (user.role === "RECRUITER") {
        links += `<li><a href="post-job.html">Post a Job</a></li>`;
    } else {
        links += `<li><a href="applications.html">My Applications</a></li>`;
    }

    links += `
        <li><a href="dashboard.html">Dashboard</a></li>
        <li><a href="profile.html">Profile</a></li>
        <li><a href="#" onclick="logout(); return false;">Logout (${user.name})</a></li>
    `;

    navLinks.innerHTML = links;
}

// ---------- Register form ----------
function initRegisterForm() {
    const form = document.getElementById("register-form");
    if (!form) return;

    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        hideMessages();

        const name = document.getElementById("reg-name").value.trim();
        const email = document.getElementById("reg-email").value.trim();
        const password = document.getElementById("reg-password").value;
        const role = document.getElementById("reg-role").value;

        try {
            const user = await apiPost("/auth/register", { name, email, password, role });
            saveLoggedInUser(user);
            showSuccess("Registration successful! Redirecting...");
            setTimeout(() => window.location.href = "dashboard.html", 800);
        } catch (err) {
            showError(err.message);
        }
    });
}

// ---------- Login form ----------
function initLoginForm() {
    const form = document.getElementById("login-form");
    if (!form) return;

    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        hideMessages();

        const email = document.getElementById("login-email").value.trim();
        const password = document.getElementById("login-password").value;

        try {
            const user = await apiPost("/auth/login", { email, password });
            saveLoggedInUser(user);
            window.location.href = "dashboard.html";
        } catch (err) {
            showError(err.message);
        }
    });
}

function showError(message) {
    const el = document.getElementById("error-msg");
    if (el) {
        el.textContent = message;
        el.style.display = "block";
    } else {
        alert(message);
    }
}

function showSuccess(message) {
    const el = document.getElementById("success-msg");
    if (el) {
        el.textContent = message;
        el.style.display = "block";
    }
}

function hideMessages() {
    const err = document.getElementById("error-msg");
    const ok = document.getElementById("success-msg");
    if (err) err.style.display = "none";
    if (ok) ok.style.display = "none";
}

document.addEventListener("DOMContentLoaded", function () {
    renderNavbar();
    initRegisterForm();
    initLoginForm();
});
