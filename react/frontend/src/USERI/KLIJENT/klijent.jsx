import { Link } from "react-router-dom";
import { useUser } from "../../UserContext.jsx";
import { API_URL, FRONTEND_URL } from "../../config.js";

function Klijent() {
  const { user } = useUser();

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

  if (!user) return <p>Učitavanje korisnika...</p>;

  return (
    <div className="page-background">
      <div className="content-container">
        <div className="header">
          <h1 className="page-title">Dobrodošli, {user.name}!</h1>
          <Link to="/podaci" style={{ textDecoration: "none", color: "blue" }}>
            Moji podaci
          </Link>
          <Link to="/nalog" style={{ textDecoration: "none", color: "blue" }}>
            Putni nalog
          </Link>
           <Link to="/faktura" style={{ textDecoration: "none", color: "blue" }}>
            Nova faktura
          </Link>
          <Link to="/racuni" style={{ textDecoration: "none", color: "blue" }}>
            Moji računi
          </Link>
          <Link to="/dodajradnika" style={{ textDecoration: "none", color: "blue" }}>
            Dodaj radnika
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

export default Klijent;
