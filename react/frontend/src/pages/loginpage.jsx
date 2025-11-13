import { useAuth0 } from "@auth0/auth0-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_URL } from "../config.js";

function LoginPage() {
  {/*const { loginWithRedirect, isAuthenticated, isLoading } = useAuth0();
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: ""
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Form submitted:", formData);
    navigate("/pocetna");
  };

  if (isLoading) {
    return (
      <div className="loading-container">
        <div className="loading-text">Učitavanje...</div>
      </div>
    );
  }

  if (isAuthenticated) {
    return (
      <div className="loading-container">
        <div className="loading-text">Već ste prijavljeni!</div>
        <button 
          onClick={() => navigate("/pocetna")}
          className="primary-button"
        >
          Idi na početnu
        </button>
      </div>
    );
  }
  */}
  return (
    <div className="page-container">
      <div className="form-container">
        <h1 className="form-title">Login</h1>
        
        <div className="auth-button-container">
          <button
            onClick={() => window.location.href = `${API_URL}/oauth2/authorization/google`}
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