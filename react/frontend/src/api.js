import { API_URL } from "./config.js";

export function getSessionToken() {
  return localStorage.getItem("sessionToken");
}

export function setSessionToken(token) {
  localStorage.setItem("sessionToken", token);
}

export function removeSessionToken() {
  localStorage.removeItem("sessionToken");
}

export async function apiCall(url, options = {}) {
  const token = getSessionToken();
  const headers = {
    ...options.headers,
  };
  
  if (token) {
    headers["X-Session-Token"] = token;
  }
  
  const response = await fetch(url, {
    ...options,
    headers,
    credentials: "include",
  });
  
  return response;
}

