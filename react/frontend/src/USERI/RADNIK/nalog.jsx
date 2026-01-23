import { useNavigate } from "react-router-dom";
import { apiCall } from "../../api.js";
import { useState, useEffect } from "react";
import { API_URL } from "../../config.js";
import { useRadnik } from "../../USERI/RADNIK/RadnikContext.jsx";
import { useKlijent } from "../../USERI/KLIJENT/KlijentContext.jsx";
import { useUser } from "../../UserContext.jsx";

function Nalog() {
  const navigate = useNavigate();
  const { user } = useUser();
  
  const radnikCtx = useRadnik();
  const klijentCtx = useKlijent();

  const firmaId = user?.role === "KLIJENT" ? klijentCtx?.firmaId : radnikCtx?.firmaId;

  const [PN, setPutniNalozi] = useState([]);
  const [PNN, setPutniNaloziNeodradjeni] = useState([]);
  const [zaposlenik, setZaposlenik] = useState(null);
  const [loading, setLoading] = useState(true);

  const jeRadnik = Array.isArray(zaposlenik);

  useEffect(() => {
    async function fetchAll() {
      if (!firmaId) {
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        // Osiguravamo da firmaId ide u query string
        const url = `${API_URL}/api/izvjestaj/listaPN?odabranaFirmaId=${firmaId}`;
        const fRes = await apiCall(url);
        
        if (fRes.ok) {
          const fakturData = await fRes.json();
          console.log("Podaci s backenda:", fakturData);
          
          if (Array.isArray(fakturData.svi)) {
            setZaposlenik(fakturData.svi);
            setPutniNalozi([]);
            setPutniNaloziNeodradjeni([]);
          } else {
            setPutniNalozi(fakturData.odradjeni ?? []);
            setPutniNaloziNeodradjeni(fakturData.neodradjeni ?? []);
            setZaposlenik(null);
          }
        }
      } catch (err) {
        alert("Greška pri dohvaćanju podataka: " + err);
      } finally {
        setLoading(false);
      }
    }

    fetchAll();
  }, [firmaId]);

  if (loading) {
    return <p>Učitavanje podataka...</p>;
  }

  return (
    <div className="nalog-container">
      <h1>Pregled putnih naloga</h1>

      <button
        onClick={() => navigate("/novinalog")}
        className="primary-button"
      >
        Novi putni nalog
      </button>

      <h3>Putni nalozi</h3>

      {jeRadnik ? (
        <>
          {zaposlenik.length === 0 ? (
            <p>Nema putnih naloga.</p>
          ) : (
            <ul>
              <p>Svi putni nalozi zaposlenika</p>
              {zaposlenik.map((nalog) => (
                <li key={nalog.idPutniNalog}>
                  Datum izdavanja: {nalog.datumIzdavanja} ||
                  Prijevozno sredstvo: {nalog.prijevoznoSredstvo} ||
                  Svrha: {nalog.svrhaPutovanja || "—"} ||
                  Trošak: {nalog.trosak || "Nepoznato"}
                </li>
              ))}
            </ul>
          )}
        </>
      ) : (
        <>
          {PN.length === 0 ? (
            <p>Nema odrađenih putnih naloga.</p>
          ) : (
            <ul>
              <p>Odradjeni putni nalozi</p>
              {PN.map((nalozi) => (
                <li key={nalozi.idPutniNalog}>
                  Datum izadavanja- {nalozi.datumIzdavanja} || 
                  Prijevozno sredstvo-{nalozi.prijevoznoSredstvo} || 
                  Svrha putovanja-{nalozi.svrhaPutovanja || "Svrha putovanja"} || 
                  Trosak: {nalozi.trosak || "Nepoznato"}
                </li>
              ))}
            </ul>
          )}

          {PNN.length === 0 ? (
            <p>Nema neodrađenih putnih naloga.</p>
          ) : (
            <ul>
              <p>Neodradjeni putni nalozi</p>
              {PNN.map((nalozi) => (
                <li key={nalozi.idPutniNalog}>
                  Datum izadavanja- {nalozi.datumIzdavanja} || 
                  Prijevozno sredstvo-{nalozi.prijevoznoSredstvo} || 
                  Svrha putovanja-{nalozi.svrhaPutovanja || "Svrha putovanja"} || 
                  Trosak: {nalozi.trosak || "Nepoznato"}
                </li>
              ))}
            </ul>
          )}
        </>
      )}
    </div>
  );
}

export default Nalog;