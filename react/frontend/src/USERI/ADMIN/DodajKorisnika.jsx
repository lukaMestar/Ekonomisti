import { useState } from "react";
import { Link } from "react-router-dom";
import "./dodajKorisnika.css";

const ROLES = ["ADMIN", "RACUNOVODA", "KLIJENT", "RADNIK"];

export default function DodajKorisnika() {
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("RACUNOVODA");

  const handleSubmit = async (event) => {
    event.preventDefault();

    const korisnikData = {
      email: email.trim(),
      role,
    };

    try {
      const response = await fetch("http://localhost:9090/api/addaccountant", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(korisnikData),
      });

      if (response.ok) {
        alert("Korisnik added successfully");
      } else {
        alert("Error adding korisnik");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error adding korisnik");
    }
  };

  return (
    <div className="dodaj-box">
      <h2>Dodaj korisnika</h2>

      <div className="dodaj-form">
        <label className="uloge">
          <span>E-mail</span>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value.trim())}
          />
        </label>

        <label className="uloge">
          <span>Uloga</span>
          <select value={role} onChange={(e) => setRole(e.target.value)}>
            {ROLES.map((r) => (
              <option key={r} value={r}>
                {r}
              </option>
            ))}
          </select>
        </label>

        <button className="btn" type="button" onClick={handleSubmit}>
          Spremi
        </button>
      </div>

      <div className="back">
        <Link to="/admin">Natrag</Link>
      </div>
    </div>
  );
}
