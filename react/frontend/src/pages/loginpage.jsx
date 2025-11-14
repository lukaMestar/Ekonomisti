import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_URL } from "../config.js";

function LoginPage() {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // Check if user is already authenticated
    fetch(`${API_URL}/api/user`, {
      method: "GET",
      credentials: "include",
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
        throw new Error("Not authenticated");
      })
      .then((data) => {
        // User is authenticated, redirect based on role
        if (data.role === "ADMIN") {
          navigate("/admin");
        } else if (data.role === "RACUNOVODA") {
          navigate("/racunovoda");
        } else if (data.role === "KLIJENT") {
          navigate("/klijent");
        } else if (data.role === "RADNIK") {
          navigate("/radnik");
        } else {
          navigate("/pocetna");
        }
      })
      .catch(() => {
        // User is not authenticated, show login page
        setLoading(false);
      });
  }, [navigate]);

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-text">Učitavanje...</div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="form-container">
        <h1 className="form-title">Login</h1>

        <div className="auth-button-container">
          <button
            onClick={() =>
              (window.location.href = `${API_URL}/oauth2/authorization/google`)
            }
            className="auth-button"
          >
            Prijavi se s Auth2.0
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
