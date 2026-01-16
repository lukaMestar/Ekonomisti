import { useState, useEffect } from "react";
import { API_URL } from "../../config";
import StripePaymentForm from "./StripePaymentForm";

function Racuni() {
  const [racuni, setRacuni] = useState([]);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(null);
  const [selectedRacun, setSelectedRacun] = useState(null);

  useEffect(() => {
    fetchRacuni();
  }, []);

  const fetchRacuni = async () => {
    try {
      const res = await fetch(`${API_URL}/api/placanje/racuni`, {
        credentials: "include",
      });
      if (res.ok) {
        const data = await res.json();
        setRacuni(data);
      } else {
        console.error("Greška pri dohvaćanju računa");
      }
    } catch (error) {
      console.error("Greška pri dohvaćanju računa:", error);
    } finally {
      setLoading(false);
    }
  };

  const handlePlati = (racun) => {
    setSelectedRacun(racun);
  };

  const handlePaymentSuccess = () => {
    setSelectedRacun(null);
    fetchRacuni(); // Osvježi listu
  };

  const handlePaymentCancel = () => {
    setSelectedRacun(null);
  };

  const neplaceni = racuni.filter((r) => r.statusPlacanja === "neplaceno");
  const placeni = racuni.filter((r) => r.statusPlacanja === "placeno");

  const formatDatum = (datum) => {
    if (!datum) return "";
    return new Date(datum).toLocaleDateString("hr-HR");
  };

  const formatIznos = (iznos) => {
    return new Intl.NumberFormat("hr-HR", {
      style: "currency",
      currency: "EUR",
    }).format(iznos);
  };

  if (loading) {
    return <div style={{ padding: "20px" }}>Učitavanje računa...</div>;
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>Pregled svih računa</h1>

      {selectedRacun ? (
        <div>
          <h2>
            Plaćanje računa za {selectedRacun.mjesec}/{selectedRacun.godina}
          </h2>
          <p>
            Iznos: <strong>{formatIznos(selectedRacun.iznos)}</strong>
          </p>
          <StripePaymentForm
            racunId={selectedRacun.idRacun}
            iznos={
              typeof selectedRacun.iznos === "number"
                ? selectedRacun.iznos
                : parseFloat(selectedRacun.iznos)
            }
            onSuccess={handlePaymentSuccess}
            onCancel={handlePaymentCancel}
          />
        </div>
      ) : (
        <>
          <h3>Neplaćeni računi</h3>
          {neplaceni.length === 0 ? (
            <p>Nema neplaćenih računa ✅</p>
          ) : (
            <ul style={{ listStyle: "none", padding: 0 }}>
              {neplaceni.map((r) => (
                <li
                  key={r.idRacun}
                  style={{
                    marginBottom: "15px",
                    padding: "10px",
                    border: "1px solid #ddd",
                    borderRadius: "4px",
                  }}
                >
                  <strong>
                    Račun za {r.mjesec}/{r.godina}
                  </strong>{" "}
                  — {formatIznos(r.iznos)} (rok: {formatDatum(r.datumRoka)}){" "}
                  <button
                    onClick={() => handlePlati(r)}
                    disabled={processing === r.idRacun}
                    style={{
                      padding: "8px 16px",
                      backgroundColor:
                        processing === r.idRacun ? "#ccc" : "#4CAF50",
                      color: "white",
                      border: "none",
                      borderRadius: "4px",
                      cursor:
                        processing === r.idRacun ? "not-allowed" : "pointer",
                      marginLeft: "10px",
                    }}
                  >
                    Plati račun 💳
                  </button>
                </li>
              ))}
            </ul>
          )}
        </>
      )}

      {!selectedRacun && (
        <>
          <h3>Svi plaćeni računi</h3>
          {placeni.length === 0 ? (
            <p>Nema plaćenih računa još ⏳</p>
          ) : (
            <ul style={{ listStyle: "none", padding: 0 }}>
              {placeni.map((r) => (
                <li
                  key={r.idRacun}
                  style={{
                    marginBottom: "10px",
                    padding: "10px",
                    border: "1px solid #ddd",
                    borderRadius: "4px",
                    backgroundColor: "#f0f0f0",
                  }}
                >
                  <strong>
                    Račun za {r.mjesec}/{r.godina}
                  </strong>{" "}
                  — {formatIznos(r.iznos)} (plaćeno:{" "}
                  {formatDatum(r.datumPlacanja)})
                </li>
              ))}
            </ul>
          )}
        </>
      )}
    </div>
  );
}

export default Racuni;
