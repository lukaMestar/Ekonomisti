// API configuration
// Ensure API_URL has protocol - Render provides hostname only
const apiHost = import.meta.env.VITE_API_URL || "http://localhost:9090";
let apiUrl = apiHost;

// If it doesn't start with http, add https://
if (!apiUrl.startsWith("http")) {
  apiUrl = `https://${apiUrl}`;
}

// If it's just a hostname without .onrender.com, add it
// Render services have format: service-name.onrender.com
if (!apiUrl.includes(".") && !apiUrl.includes("localhost")) {
  apiUrl = `https://${apiHost}.onrender.com`;
} else if (!apiUrl.includes("://") && !apiUrl.includes("localhost")) {
  // If it's missing protocol but has dots, add https://
  apiUrl = `https://${apiHost}`;
}

export const API_URL = apiUrl;

const frontendHost =
  import.meta.env.VITE_FRONTEND_URL || "http://localhost:5173";
let frontendUrl = frontendHost;

// If it doesn't start with http, add https://
if (!frontendUrl.startsWith("http")) {
  frontendUrl = `https://${frontendUrl}`;
}

// If it's just a hostname without .onrender.com, add it
if (!frontendUrl.includes(".") && !frontendUrl.includes("localhost")) {
  frontendUrl = `https://${frontendHost}.onrender.com`;
} else if (!frontendUrl.includes("://") && !frontendUrl.includes("localhost")) {
  frontendUrl = `https://${frontendHost}`;
}

export const FRONTEND_URL = frontendUrl;
