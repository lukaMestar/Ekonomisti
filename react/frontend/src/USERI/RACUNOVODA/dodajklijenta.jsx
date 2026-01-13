import { useRacunovoda } from "./RacunovodaContext";
import { useState } from "react";
import { API_URL } from "../../config";

function DodajKlijenta() {
  const { slobodniKlijenti, setSlobodniKlijenti } = useRacunovoda();
  const [mjesecniTrosak, setMjesecniTrosak] = useState({});
  const [loadingId, setLoadingId] = useState(null);
  const [error, setError] = useState("");

  const handleAddClient = async (klijentId) => {
    const trosak = mjesecniTrosak[klijentId];

    if (!trosak || Number(trosak) <= 0) {
      setError("Unesite ispravan mjesečni trošak.");
      return;
    }

    setLoadingId(klijentId);
    setError("");

    try {
      const res = await fetch(
        `${API_URL}/api/racunovoda/dodaj-klijenta`,
        {
          method: "POST",
          credentials: "include", // 🔐 OAuth2 cookie
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            klijentId,
            mjesecniTrosakUsluge: Number(trosak),
          }),
        }
      );

      if (!res.ok) throw new Error("Greška pri dodavanju klijenta");

      // remove client from free list
      setSlobodniKlijenti(prev =>
        prev.filter(k => k.id !== klijentId)
      );

    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingId(null);
    }
  };

  return (
    <div>
      <h1>Dodaj klijenta</h1>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <ul>
        {slobodniKlijenti.length === 0 && (
          <p>Nema slobodnih klijenata.</p>
        )}

        {slobodniKlijenti.map(k => (
          <li key={k.id}>
            {k.ime}

            <input
              type="number"
              placeholder="Mjesečni trošak"
              min="0"
              step="0.01"
              value={mjesecniTrosak[k.id] || ""}
              onChange={(e) =>
                setMjesecniTrosak(prev => ({
                  ...prev,
                  [k.id]: e.target.value,
                }))
              }
              style={{ marginLeft: "10px" }}
            />

            <button
              onClick={() => handleAddClient(k.id)}
              disabled={loadingId === k.id}
              style={{ marginLeft: "10px" }}
            >
              {loadingId === k.id ? "Dodajem..." : "Dodaj"}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default DodajKlijenta;
