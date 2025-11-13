import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(/*{
      babel: {
        plugins: ['babel-plugin-react-compiler'],
      },
    }*/),
  ],
  preview: {
    host: "0.0.0.0",
    port: 10000,
    allowedHosts: ["ekonomisti-frontend.onrender.com", ".onrender.com"],
  },
});
