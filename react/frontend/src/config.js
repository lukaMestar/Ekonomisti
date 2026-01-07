// Detekcija okruženja
const isDevelopment = import.meta.env.DEV || 
                      window.location.hostname === 'localhost' || 
                      window.location.hostname === '127.0.0.1';

// API URL konfiguracija
let apiUrl;
const apiHost = import.meta.env.VITE_API_URL;

if (apiHost) {
  // Ako je postavljen VITE_API_URL
  if (apiHost.startsWith('/')) {
    // Relativna putanja (produkcija s nginx-om) - koristi kao-je
    apiUrl = apiHost;
  } else if (apiHost.startsWith('http')) {
    // Puna URL adresa (lokalno Docker ili custom)
    apiUrl = apiHost;
  } else {
    // Fallback - dodaj protokol
    if (apiHost.includes("localhost") || apiHost.includes("127.0.0.1")) {
      apiUrl = `http://${apiHost}`;
    } else {
      apiUrl = isDevelopment ? `http://${apiHost}` : `https://${apiHost}`;
    }
  }
} else {
  // Default ponašanje
  if (isDevelopment) {
    // Lokalno razvijanje - direktna konekcija na backend
    apiUrl = 'http://localhost:9090';
  } else {
    // Produkcija - relativna putanja kroz nginx
    apiUrl = '/backend';
  }
}

export const API_URL = apiUrl;

// Frontend URL konfiguracija
const frontendHost = import.meta.env.VITE_FRONTEND_URL;
let frontendUrl;

if (frontendHost) {
  if (frontendHost.startsWith('http')) {
    frontendUrl = frontendHost;
  } else {
    if (frontendHost.includes("localhost") || frontendHost.includes("127.0.0.1")) {
      frontendUrl = `http://${frontendHost}`;
    } else {
      frontendUrl = isDevelopment ? `http://${frontendHost}` : `https://${frontendHost}`;
    }
  }
} else {
  // Default ponašanje
  if (isDevelopment) {
    frontendUrl = 'http://localhost:5173';
  } else {
    // Produkcija - koristi trenutni hostname
    frontendUrl = `${window.location.protocol}//${window.location.host}`;
  }
}

export const FRONTEND_URL = frontendUrl;
