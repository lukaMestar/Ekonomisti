import { useState } from "react";
import { useUser } from "../../UserContext.jsx";
import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";

function DodajRadnika() {
  const [email, setEmail] = useState("");
  const [imeInput, setImeInput] = useState("");
  const [placa, setPlaca] = useState("");
  const { user } = useUser();

  const handleSubmit = async (event) => {
    event.preventDefault();

    const dijeloviImena = imeInput.trim().split(" ");
    const imeRadnika = dijeloviImena[0];
    const prezimeRadnika = dijeloviImena.slice(1).join(" ") || " ";

    try {
      const response = await apiCall(`${API_URL}/api/adduser`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: email.trim(),
          idUloge: 4,
          imeKorisnik: imeRadnika,
          prezimeKorisnik: prezimeRadnika,
        }),
      });

      if (!response.ok) {
        const errorMsg = await response.text();
        throw new Error(errorMsg);
      }

      const noviIdRadnika = parseInt(await response.text(), 10);

      const response2 = await apiCall(`${API_URL}/api/addzaposlenik`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          idKorisnika: noviIdRadnika,
          placa: parseFloat(placa),
        }),
      });

      if (!response2.ok) throw new Error("Greška pri kreiranju zaposlenika.");

      const firmaRes = await apiCall(`${API_URL}/api/klijent`);
      if (!firmaRes.ok) throw new Error("Nije moguće dohvatiti ID firme.");
      
      const firmaData = await firmaRes.json();
      const fId = firmaData.idFirma || firmaData.firmaId;

      const response3 = await apiCall(`${API_URL}/api/addjezaposlen`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          idZaposlenik: noviIdRadnika,
          idFirma: fId,
          idKlijent: user.idKorisnika || user.id,
        }),
      });

      const response4 = await apiCall(`${API_URL}/api/addplaca`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          idZaposlenik: noviIdRadnika,
          idFirma: fId,
          iznosPlace: parseFloat(placa),
        }),
      });

      if (response3.ok && response4.ok) {
        alert("Radnik uspješno dodan s imenom: " + imeRadnika + " " + prezimeRadnika);
        setEmail("");
        setImeInput("");
        setPlaca("");
      } else {
        alert("Radnik dodan, ali povezivanje s firmom nije uspjelo.");
      }

    } catch (error) {
      alert("Error: " + error.message);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Dodaj Radnika</h1>
      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px", maxWidth: "300px" }}>
        <div>
          <label>Ime i prezime:</label>
          <input
            type="text"
            value={imeInput}
            onChange={(e) => setImeInput(e.target.value)}
            placeholder="npr. Ivan Ivić"
            required
          />
        </div>

        <div>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="email@domena.com"
            required
          />
        </div>

        <div>
          <label>Plaća (€):</label>
          <input
            type="number"
            value={placa}
            onChange={(e) => setPlaca(e.target.value)}
            step="0.01"
            placeholder="1000.00"
            required
          />
        </div>

        <button type="submit" style={{ cursor: "pointer", padding: "10px" }}>
          Spremi Radnika
        </button>
      </form>
    </div>
  );
}

export default DodajRadnika;