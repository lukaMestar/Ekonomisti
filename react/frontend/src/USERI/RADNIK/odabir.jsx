import { useState } from "react";

function Odabir({ opcije, onOdaberi }) {
  const [odabrano, setOdabrano] = useState("");

  if (!opcije || !Array.isArray(opcije) || opcije.length === 0) {
    return <p>Učitavanje opcija...</p>;
  }

  const handleChange = (e) => {
    const value = e.target.value;
    setOdabrano(value);
    if (value) {
      onOdaberi(value);
    }
  };

  return (
    <div style={{ padding: "20px", textAlign: "center" }}>
      <h1>Molim odaberite svoju tvrtku</h1>
      <div style={{ width: "200px", margin: "0 auto" }}>
        <select
          value={odabrano}
          onChange={handleChange}
          style={{
            width: "100%",
            padding: "10px",
            border: "1px solid #ccc",
            borderRadius: "5px",
            background: "#fff",
          }}
        >
          <option value="" disabled>
            Odaberi tvrtku
          </option>
          {opcije.map((option) => (
            <option
              key={`${option.idFirma}-${option.idKlijent}`}
              value={`${option.idFirma}-${option.idKlijent}`}
            >
            {option.naziv}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
}

export default Odabir;
