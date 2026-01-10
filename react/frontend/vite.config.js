import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "path";

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // Učitaj .env iz root direktorija projekta (2 nivoa gore)
  const envDir = resolve(__dirname, "../..");
  const env = loadEnv(mode, envDir, "");

  return {
    plugins: [
      react(/*{
        babel: {
          plugins: ['babel-plugin-react-compiler'],
        },
      }*/),
    ],
    envDir: envDir, // Koristi root .env datoteku
    preview: {
      host: "0.0.0.0",
      port: process.env.PORT ? parseInt(process.env.PORT) : 10000,
    },
  };
});
