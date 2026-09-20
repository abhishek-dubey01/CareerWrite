// ===================================================
// api.js
// One place that knows the backend base URL and wraps fetch()
// so every other JS file just calls apiGet()/apiPost()/apiPut()/apiDelete().
// ===================================================

const API_BASE_URL = "http://localhost:8080/api";

// Generic request helper. Throws an Error with the backend's message
// (from our ApiError { message: "..." } shape) if the response is not OK.
async function apiRequest(path, method, body) {
    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json"
        }
    };

    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(API_BASE_URL + path, options);

    // 204 No Content - nothing to parse
    if (response.status === 204) {
        return null;
    }

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        const message = (data && data.message) ? data.message : "Something went wrong. Please try again.";
        throw new Error(message);
    }

    return data;
}

function apiGet(path) {
    return apiRequest(path, "GET");
}

function apiPost(path, body) {
    return apiRequest(path, "POST", body);
}

function apiPut(path, body) {
    return apiRequest(path, "PUT", body);
}

function apiDelete(path) {
    return apiRequest(path, "DELETE");
}
