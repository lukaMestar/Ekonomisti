import { useAuth0 } from "@auth0/auth0-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {
  const { loginWithRedirect, isAuthenticated, isLoading } = useAuth0();
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

  return (
    <div className="page-container">
      <div className="form-container">
        <h1 className="form-title">Registracija</h1>
        
        <div className="auth-button-container">
          <button
            onClick={() => loginWithRedirect()}
            className="auth-button"
          >
            Registriraj se s Auth0
          </button>
        </div>

        <form onSubmit={handleSubmit} className="form">
          <input
            type="text"
            name="name"
            placeholder="Ime i prezime"
            value={formData.name}
            onChange={handleChange}
            className="form-input"
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email adresa"
            value={formData.email}
            onChange={handleChange}
            className="form-input"
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Lozinka"
            value={formData.password}
            onChange={handleChange}
            className="form-input"
            required
          />
          <button
            type="submit"
            className="submit-button"
          >
            Registriraj se
          </button>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;