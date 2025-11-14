const apiHost = import.meta.env.VITE_API_URL || "http://localhost:9090";
let apiUrl = apiHost;

if (!apiUrl.startsWith("http")) {
  apiUrl = `https://${apiUrl}`;
}

if (!apiUrl.includes(".") && !apiUrl.includes("localhost")) {
  apiUrl = `https://${apiHost}.onrender.com`;
} else if (!apiUrl.includes("://") && !apiUrl.includes("localhost")) {
  apiUrl = `https://${apiHost}`;
}

export const API_URL = apiUrl;

const frontendHost =
  import.meta.env.VITE_FRONTEND_URL || "http://localhost:5173";
let frontendUrl = frontendHost;

if (!frontendUrl.startsWith("http")) {
  frontendUrl = `https://${frontendUrl}`;
}

if (!frontendUrl.includes(".") && !frontendUrl.includes("localhost")) {
  frontendUrl = `https://${frontendHost}.onrender.com`;
} else if (!frontendUrl.includes("://") && !frontendUrl.includes("localhost")) {
  frontendUrl = `https://${frontendHost}`;
}

export const FRONTEND_URL = frontendUrl;
