// API configuration
// Ensure API_URL has protocol - Render provides hostname only
const apiHost = import.meta.env.VITE_API_URL || 'http://localhost:9090';
export const API_URL = apiHost.startsWith('http') ? apiHost : `https://${apiHost}`;

const frontendHost = import.meta.env.VITE_FRONTEND_URL || 'http://localhost:5173';
export const FRONTEND_URL = frontendHost.startsWith('http') ? frontendHost : `https://${frontendHost}`;

