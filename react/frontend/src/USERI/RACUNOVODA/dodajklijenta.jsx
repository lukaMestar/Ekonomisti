import { useRacunovoda } from "./RacunovodaContext";
import { useState } from "react";
import { API_URL } from "../../config";

function DodajKlijenta() {
  const [form, setForm] = useState({
    ime: "",
    prezime: "",
    email: "",
    nazivFirme: "",
    emailIzvjestaj: "",
    pocetnoStanje: "",
    mjesecniTrosakUsluge: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!form.ime || !form.prezime || !form.email || !form.nazivFirme) {
      setError("Sva obavezna polja moraju biti popunjena.");
      return;
    }

    setLoading(true);

    try {
      const pocetnoStanjeNum = Number(form.pocetnoStanje) || 0;
      const mjesecniTrosakNum = Number(form.mjesecniTrosakUsluge) || 0;
      
      const res = await fetch(`${API_URL}/api/racunovoda/novi-klijent`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...form,
          pocetnoStanje: isNaN(pocetnoStanjeNum) ? 0 : pocetnoStanjeNum,
          mjesecniTrosakUsluge: isNaN(mjesecniTrosakNum) ? 0 : mjesecniTrosakNum,
        }),
      });

      if (!res.ok) throw new Error("Greška pri dodavanju klijenta");

      alert("Klijent uspješno dodan!");
      setForm({
        ime: "",
        prezime: "",
        email: "",
        nazivFirme: "",
        emailIzvjestaj: "",
        pocetnoStanje: "",
        mjesecniTrosakUsluge: "",
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Novi klijent</h1>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <input name="ime" placeholder="Ime" value={form.ime} onChange={handleChange} />
      <input name="prezime" placeholder="Prezime" value={form.prezime} onChange={handleChange} />
      <input name="email" placeholder="Email" value={form.email} onChange={handleChange} />

      <hr />

      <input name="nazivFirme" placeholder="Naziv firme" value={form.nazivFirme} onChange={handleChange} />
      <input name="emailIzvjestaj" placeholder="Email za izvještaje" value={form.emailIzvjestaj} onChange={handleChange} />
      <input name="pocetnoStanje" type="number" step="0.01" placeholder="Početno stanje" value={form.pocetnoStanje} onChange={handleChange} />

      <hr />

      <input
        name="mjesecniTrosakUsluge"
        type="number"
        step="0.01"
        placeholder="Mjesečni trošak usluge"
        value={form.mjesecniTrosakUsluge}
        onChange={handleChange}
      />

      <button disabled={loading}>
        {loading ? "Spremam..." : "Dodaj klijenta"}
      </button>
    </form>
  );
}

export default DodajKlijenta;