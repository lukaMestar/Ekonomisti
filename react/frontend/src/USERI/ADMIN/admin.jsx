import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useUser } from "../../UserContext.jsx";
import { API_URL, FRONTEND_URL } from "../../config.js";

function Admin() {
  const { user, setUser, tvrtke, trenutnaTvrtka, setTrenutnaTvrtka } =
    useUser();
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // If user is not loaded, try to fetch it
    if (!user) {
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
          setUser(data);
          setLoading(false);
          // If user is not admin, redirect to appropriate page
          if (data.role !== "ADMIN") {
            if (data.role === "RACUNOVODA") {
              navigate("/racunovoda");
            } else if (data.role === "KLIJENT") {
              navigate("/klijent");
            } else if (data.role === "RADNIK") {
              navigate("/radnik");
            } else {
              navigate("/pocetna");
            }
          }
        })
        .catch(() => {
          // User is not authenticated, redirect to login
          setLoading(false);
          navigate("/");
        });
    } else {
      setLoading(false);
    }
  }, [user, setUser, navigate]);

  const handleLogout = async () => {
    try {
      await fetch(`${API_URL}/logout`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      });
      window.location.href = FRONTEND_URL;
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  if (loading || !user) return <p>Učitavanje korisnika...</p>;

  return (
    <div className="page-background">
      <div className="content-container">
        <div className="header">
          <h1 className="page-title">Dobrodošli, {user.name}!</h1>

          <Link
            to="/DodajKorisnika"
            style={{ textDecoration: "none", color: "blue" }}
          >
            Dodaj korisnika
          </Link>
          <Link
            to="/ListaKorisnika"
            style={{ textDecoration: "none", color: "blue" }}
          >
            Upravljaj korisnicima
          </Link>
          <Link
            to="/Aktivnosti"
            style={{ textDecoration: "none", color: "blue" }}
          >
            Aktivnosti
          </Link>
          <button onClick={handleLogout} className="logout-button">
            Odjava
          </button>
        </div>

        <div className="success-message">
          <h2 className="success-title">Uspješno ste prijavljeni!</h2>
          <p className="success-text">
            Dobrodošli, <strong>{user.name}</strong>
          </p>
        </div>

        <div className="info-grid">
          <div className="info-card">
            <h3 className="info-title">Korisnički podaci:</h3>
            <p>
              <strong>Ime:</strong> {user.name}
            </p>
            <p>
              <strong>Email:</strong> {user.email}
            </p>
          </div>
          {user.picture && (
            <div className="info-card">
              <img
                src={user.picture}
                alt="Profil"
                width="100"
                style={{ borderRadius: "50%" }}
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Admin;
