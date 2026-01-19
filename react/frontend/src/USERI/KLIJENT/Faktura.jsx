
import { useNavigate } from "react-router-dom";
import { apiCall } from "../../api.js";
import { useState, useEffect } from "react";
import { API_URL } from "../../config.js";

function Faktura() {
  const navigate = useNavigate();
  const[F, setF] = useState([]);
  const[FN, setFN] = useState([]);

  const [loading, setLoading] = useState(true); 
    useEffect(() => {
    async function fetchAll() {
      try {
        const fRes = await apiCall(`${API_URL}/api/izvjestaj/listaF`);
        if (fRes.ok) {
          const fakturData = await fRes.json();
          setF(fakturData.odradjeni ?? []);
          setFN(fakturData.neodradjeni ?? []);
        }
      } catch (err) {
        alert("Greška pri dohvaćanju podataka: " + err);
      } finally {
        setLoading(false);
      }
    }

    fetchAll();
  }, []);
  
    
    console.log("Tuu saammmmmm --------------------------");
  
    if (loading) { return <p>Učitavanje podataka...</p>;}


  
  return (
    <div>
      <h1>Pregled faktura</h1>

      <h3>Napravi novu fakturu</h3>

      <button
        onClick={() =>  navigate("/novafaktura")}
        className="primary-button"
      >
        Nova faktura
      </button>
      <h3>Odradjene fakture</h3>
      {F.length === 0 ? (
        <p>Nema odrađenih faktura.</p>
      ) : (
        <ul>
          {F.map((f) => (
            <li key={f.idFaktura}>
              Datum fakture: {f.datum} – {f.iznos ?? "iznos"} €
            </li>
          ))}
        </ul>
      )}

      <h3>Neodrađene fakture</h3>
      {FN.length === 0 ? (
        <p>Nema neodrađenih faktura.</p>
      ) : (
        <ul>
          {FN.map((f) => (
            <li key={f.idFaktura}>
              Datum fakture: {f.datum} – {f.iznos ?? "iznos"} €
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}


export default Faktura;   
