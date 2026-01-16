import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { useUser } from "../../UserContext";
import { useRacunovoda } from "./RacunovodaContext";

import { API_URL } from "../../config";

function Racunklijenti() {
  const navigate = useNavigate();
  const { klijenti, oznaciOdradjen } = useRacunovoda();

  // Lokalni state za odabir
  const [odabraniKlijent, setOdabraniKlijent] = useState("");
  const [odabraniMjesec, setOdabraniMjesec] = useState("");
  const [generiranjeRacuna, setGeneriranjeRacuna] = useState({});

  const handleIzvjestaj = () => {
    if (!odabraniKlijent || !odabraniMjesec) {
      alert("Molim odaberite klijenta i mjesec!");
      return;
    }

    navigate(`/izvjestaj/${odabraniKlijent}/${odabraniMjesec}`);
  };

  const handleGenerirajRacun = async (klijentId) => {
    if (
      !confirm("Jeste li sigurni da želite generirati račun za ovog klijenta?")
    ) {
      return;
    }

    setGeneriranjeRacuna((prev) => ({ ...prev, [klijentId]: true }));

    try {
      const res = await fetch(
        `${API_URL}/api/racunovoda/generiraj-racun/${klijentId}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (res.ok) {
        const message = await res.text();
        alert(message);
      } else {
        const error = await res.text();
        alert("Greška: " + error);
      }
    } catch (error) {
      console.error("Greška:", error);
      alert("Greška pri generiranju računa. Pokušajte ponovno.");
    } finally {
      setGeneriranjeRacuna((prev) => ({ ...prev, [klijentId]: false }));
    }
  };

  return (
    <div>
      <h1>Moji klijenti</h1>

      <h3>Dodaj novog klijenta</h3>
      <button
        onClick={() => navigate("/dodajklijenta")}
        className="primary-button"
      >
        Dodaj klijenta
      </button>

      <h3>Lista mojih klijenata</h3>
      <ul>
        {klijenti.map((k) => (
          <li key={k.id} style={{ marginBottom: "10px" }}>
            <Link
              to={`/klijent/${k.id}`}
              style={{ textDecoration: "none", color: "blue" }}
            >
              {k.ime}
            </Link>
            <span style={{ color: k.status === "Odrađen" ? "green" : "gray" }}>
              {k.status}
            </span>
            <button
              onClick={() => oznaciOdradjen(k.id)}
              style={{
                marginLeft: "10px",
                color: "grey",
                borderRadius: "6px",
                cursor: "pointer",
              }}
            >
              {k.status === "Odrađen" ? "Poništi" : "Označi kao odrađen"}
            </button>
            <button
              onClick={() => handleGenerirajRacun(k.id)}
              disabled={generiranjeRacuna[k.id]}
              style={{
                marginLeft: "10px",
                borderRadius: "6px",
                cursor: generiranjeRacuna[k.id] ? "not-allowed" : "pointer",
                backgroundColor: generiranjeRacuna[k.id] ? "#ccc" : "#28a745",
                color: "white",
                border: "none",
                padding: "5px 15px",
              }}
            >
              {generiranjeRacuna[k.id] ? "Generiranje..." : "Generiraj račun"}
            </button>
          </li>
        ))}
      </ul>

      <hr style={{ margin: "20px 0" }} />

      {/*Izvještaj po klijentu i mjesecu */}
      <h3>Računovodstveni izvještaj</h3>

      <div style={{ marginBottom: "10px" }}>
        <label>
          Klijent:
          <select
            value={odabraniKlijent}
            onChange={(e) => setOdabraniKlijent(e.target.value)}
            style={{ marginLeft: "10px" }}
          >
            <option value="">-- Odaberi klijenta --</option>
            {klijenti.map((k) => (
              <option key={k.id} value={k.id}>
                {k.ime}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div style={{ marginBottom: "10px" }}>
        <label>
          Mjesec:
          <select
            value={odabraniMjesec}
            onChange={(e) => setOdabraniMjesec(e.target.value)}
            style={{ marginLeft: "10px" }}
          >
            <option value="">-- Odaberi mjesec --</option>
            <option value="1">Siječanj</option>
            <option value="2">Veljača</option>
            <option value="3">Ožujak</option>
            <option value="4">Travanj</option>
            <option value="5">Svibanj</option>
            <option value="6">Lipanj</option>
            <option value="7">Srpanj</option>
            <option value="8">Kolovoz</option>
            <option value="9">Rujan</option>
            <option value="10">Listopad</option>
            <option value="11">Studeni</option>
            <option value="12">Prosinac</option>
          </select>
        </label>
      </div>

      <button onClick={handleIzvjestaj} className="primary-button">
        Prikaži izvještaj
      </button>
    </div>
  );
}

export default Racunklijenti;
