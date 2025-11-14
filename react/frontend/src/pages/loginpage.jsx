import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { API_URL } from "../config.js";

function LoginPage() {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    // Check if JWT token is in URL (after OAuth redirect)
    const token = searchParams.get("token");
    if (token) {
      console.log("JWT token received, saving to localStorage");
      localStorage.setItem("jwt_token", token);
      // Remove token from URL
      window.history.replaceState({}, document.title, window.location.pathname);
    }

    // Check if user is already authenticated (has token)
    const savedToken = localStorage.getItem("jwt_token");
    if (savedToken) {
      fetch(`${API_URL}/api/user`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${savedToken}`,
        },
      })
        .then((res) => {
          if (res.ok) {
            return res.json();
          }
          // Token invalid, remove it
          localStorage.removeItem("jwt_token");
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
    } else {
      // No token, show login page
      setLoading(false);
    }
  }, [navigate, searchParams]);

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
