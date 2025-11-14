import { API_URL } from "./config.js";

// Helper function to get JWT token from localStorage
export function getAuthToken() {
  return localStorage.getItem("jwt_token");
}

// Helper function to make authenticated API calls
export async function apiCall(url, options = {}) {
  const token = getAuthToken();
  const headers = {
    ...options.headers,
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  return fetch(`${API_URL}${url}`, {
    ...options,
    headers,
  });
}

// Helper function to logout (remove token)
export function logout() {
  localStorage.removeItem("jwt_token");
  window.location.href = "/";
}
