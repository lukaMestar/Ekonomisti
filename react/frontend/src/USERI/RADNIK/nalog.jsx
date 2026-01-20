
import { useNavigate } from "react-router-dom";
import { apiCall } from "../../api.js";
import { useState, useEffect } from "react";
import { API_URL } from "../../config.js";
function Nalog() {


  const navigate = useNavigate();
  const[PN, setPutniNalozi] = useState([]);
  const[PNN, setPutniNaloziNeodradjeni] = useState([]);

  const [loading, setLoading] = useState(true); 

    useEffect(() => {
    async function fetchAll() {
      try {
        const fRes = await apiCall(`${API_URL}/api/izvjestaj/listaPN`);
        if (fRes.ok) {
          const fakturData = await fRes.json();
          setPutniNalozi(fakturData.odradjeni ?? []);
          setPutniNaloziNeodradjeni(fakturData.neodradjeni ?? []);
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
      <h1>Pregled putnih naloga</h1>

      <h3>Napravi novi putni nalog</h3>

      <button
        onClick={() =>  navigate("/novinalog")}
        className="primary-button"
      >
        Novi putni nalog
      </button>
      <h3>Putni nalozi</h3>
      {Array.isArray(PN) && PN.length === 0 ? (
        <p>Nema odrađenih putnih naloga.</p>
      ) : (
        <ul>
          <p> Odradjeni putni nalozi</p>
           {(PN ??[]).map(nalozi => (
            <li key={nalozi.idPutniNalog}>
              {nalozi.odrediste || "Putni nalog"} : {nalozi.trosak || "Nepoznato"}
            </li>
          ))}
        </ul>
          )}
       

    {Array.isArray(PNN) && PNN.length === 0 ? (
        <p>Nema neodrađenih putnih naloga.</p>
      ) : (
        <ul>
          <p> Neodradjeni putni nalozi </p>
           {(PNN ?? []).map(nalozi => (
            <li key={nalozi.idPutniNalog}>
              {nalozi.odrediste || "Putni nalog"} : {nalozi.trosak || "Nepoznato"}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}


export default Nalog;   
