import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../RADNIK/radnik.css"

function NovaFaktura() {
  const navigate = useNavigate();

  const [faktura, setFaktura] = useState({
    brojFakture: "",
    datum: "",
    dobavljac: "",
    stavke: [{ opis: "", kolicina: 1, cijena: 0 }],
    napomene: "",
  });

  const handleStavkaChange = (index, field, value) => {
    const newStavke = [...faktura.stavke];
    newStavke[index][field] = field === "kolicina" || field === "cijena" ? parseFloat(value) : value;
    setFaktura({ ...faktura, stavke: newStavke });
  };

  const addStavka = () => {
    setFaktura({ ...faktura, stavke: [...faktura.stavke, { opis: "", kolicina: 1, cijena: 0 }] });
  };

  const removeStavka = (index) => {
    const newStavke = faktura.stavke.filter((_, i) => i !== index);
    setFaktura({ ...faktura, stavke: newStavke });
  };

  const getUkupno = () => {
    return faktura.stavke.reduce((total, stavka) => total + stavka.kolicina * stavka.cijena, 0).toFixed(2);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      /* 
      const response = await fetch("#", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(faktura),
      });
      if (!response.ok) throw new Error("Neuspješno spremanje fakture");
      alert("Faktura je uspješno spremljena!");
      */
      navigate(-1);
    } catch (error) {
      /* console.error(error); alert("Greška pri spremanju fakture"); */
      navigate(-1);
    }
  };

  return (
    <div className="nalog-container">
      <h1>Nova Faktura</h1>
      <form onSubmit={handleSubmit} className="nalog-form">
        <div className="form-group">
          <label>Broj fakture:</label>
          <input
            type="text"
            value={faktura.brojFakture}
            onChange={(e) => setFaktura({ ...faktura, brojFakture: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label>Datum:</label>
          <input
            type="date"
            value={faktura.datum}
            onChange={(e) => setFaktura({ ...faktura, datum: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label>Dobavljač:</label>
          <input
            type="text"
            value={faktura.dobavljac}
            onChange={(e) => setFaktura({ ...faktura, dobavljac: e.target.value })}
            required
          />
        </div>

       <h3>Stavke</h3>
<div className="stavke-container">
  {faktura.stavke.map((stavka, index) => (
    <div key={index} className="stavka-card">
      <div className="stavka-field">
        <label>Opis:</label>
        <input
          type="text"
          value={stavka.opis}
          onChange={(e) => handleStavkaChange(index, "opis", e.target.value)}
          placeholder="Opis stavke"
          required
        />
      </div>

      <div className="stavka-field">
        <label>Količina:</label>
        <input
          type="number"
          value={stavka.kolicina}
          onChange={(e) => handleStavkaChange(index, "kolicina", e.target.value)}
          min="1"
          required
        />
      </div>

      <div className="stavka-field">
        <label>Cijena (€):</label>
        <input
          type="number"
          value={stavka.cijena}
          onChange={(e) => handleStavkaChange(index, "cijena", e.target.value)}
          min="0"
          step="0.01"
          required
        />
      </div>

      <div className="stavka-actions">
        {faktura.stavke.length > 1 && (
          <button type="button" onClick={() => removeStavka(index)}>Ukloni</button>
        )}
      </div>
    </div>
  ))}

  <button type="button" onClick={addStavka} className="add-button">
    Dodaj stavku
  </button>
</div>



        <div className="form-group">
          <label>Ukupno (€): {getUkupno()}</label>
        </div>

        <div className="form-group">
          <label>Napomene:</label>
          <textarea
            value={faktura.napomene}
            onChange={(e) => setFaktura({ ...faktura, napomene: e.target.value })}
            rows="3"
          />
        </div>

        <button type="submit" className="submit-button">Spremi fakturu</button>
      </form>
    </div>
  );
}

export default NovaFaktura;
