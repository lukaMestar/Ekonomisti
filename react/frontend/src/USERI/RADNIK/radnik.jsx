import { useUser } from "../../UserContext.jsx";
import { useRadnik } from "../../USERI/RADNIK/RadnikContext.jsx";
import Odabir from "./odabir.jsx";
import { Link } from "react-router-dom";
import { API_URL, FRONTEND_URL } from "../../config.js";

function Radnik() {
  const { user } = useUser();
  const radnikContext = useRadnik();

  if (!radnikContext) return <p>Učitavanje tvrtki...</p>;

  const { tvrtke, trenutnaTvrtka, setTrenutnaTvrtka } = radnikContext;

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
  if (!tvrtke || tvrtke.length === 0) return <p>Učitavanje tvrtki...</p>;

  if (!trenutnaTvrtka) {
    return (
      <Odabir
        opcije={tvrtke}
        onOdaberi={(value) => {
          const [idFirma, idKlijent] = value.split("-").map(Number);
          const tvrtka = tvrtke.find(
            (t) => t.idFirma === idFirma && t.idKlijent === idKlijent
          );
          setTrenutnaTvrtka(tvrtka);
        }}
      />
    );
  }

  return (
    <div className="page-background">
      <div className="content-container">
        <div className="header">
          <h1 className="page-title">Dobrodošli, {user.name}!</h1>
          <Link to="/nalog" style={{ textDecoration: "none", color: "blue" }}>
            Putni nalog
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
            <p><strong>Ime:</strong> {user.name}</p>
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Tvrtka:</strong> {trenutnaTvrtka?.naziv}</p>
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

export default Radnik;
