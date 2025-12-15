import { useState } from "react";
import { Link } from "react-router-dom";
import "./dodajKorisnika.css";
import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";

//const ROLES = ["ADMIN", "RACUNOVODA", "KLIJENT", "RADNIK"];

const ROLE_MAP = {
  ADMIN: 1,
  RACUNOVODA: 2,
  KLIJENT: 3,
  RADNIK: 4,
};

export default function DodajKorisnika() {
  const [email, setEmail] = useState("");
  const [idUloge, setRole] = useState(2);

  const handleRoleChange = (event) => {
    const selectedRole = event.target.value;
    setRole(ROLE_MAP[selectedRole]);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const korisnikData = {
      email: email.trim(),
      idUloge,
    };

    try {
      const response = await apiCall(`${API_URL}/api/adduser`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(korisnikData),
      });

      const responseText = await response.text();

      if (response.ok) {
        alert("Korisnik added successfully");
        setEmail("");
      } else {
        alert("Error adding korisnik: " + responseText);
      }
    } catch (error) {
      alert("Error adding korisnik: " + error.message);
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
          <select
            value={Object.keys(ROLE_MAP).find(
              (key) => ROLE_MAP[key] === idUloge
            )}
            onChange={handleRoleChange}
          >
            {Object.keys(ROLE_MAP).map((roleName) => (
              <option key={roleName} value={roleName}>
                {roleName}
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
