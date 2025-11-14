import { useEffect } from "react";
import { Link } from "react-router-dom";
import { useUser } from "../../UserContext.jsx";
import { logout } from "../../api.js";

function Admin() {
  const { user, tvrtke, trenutnaTvrtka, setTrenutnaTvrtka } = useUser();

  const handleLogout = () => {
    logout();
  };

  if (!user) return <p>Učitavanje korisnika...</p>;

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
