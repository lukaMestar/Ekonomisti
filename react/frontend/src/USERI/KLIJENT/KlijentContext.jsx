import { createContext, useContext, useState, useEffect } from "react";
import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";

const KlijentContext = createContext();

export function KlijentProvider({ children }) {
  const [firmaNaziv, setFirmaNaziv] = useState(null);
  const [stanjeRac, setStanjeRac] = useState(null);
  const [emailIzvj, setEmailIzvj] = useState(null);
  const [userId, setUserId] = useState(null);
  const [firmaId, setFirmaId] = useState(null);

  const [listaZaposlenika, setListaZaposlenika] = useState([]);
  const [listaRacuna, setListaRacuna] = useState([]);
  const [PN, setPutniNalozi] = useState([]);
  const [PNN, setPutniNaloziNeodradjeni] = useState([]);
  const [F, setF] = useState([]);
  const [FN, setFN] = useState([]);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchAll() {
      try {
        const [
          firmaRes,
          racuniRes,
          zaposleniciRes,
          fRes,
          pnRes
        ] = await Promise.all([
          apiCall(`${API_URL}/api/klijent`),
          apiCall(`${API_URL}/api/izvjestaj/dohvacanjeRacuna`),
          apiCall(`${API_URL}/api/izvjestaj/dohvacanjeZaposlenika`),
          apiCall(`${API_URL}/api/izvjestaj/listaF`),
          apiCall(`${API_URL}/api/izvjestaj/listaPN`)
        ]);

        if (firmaRes.ok) {
          const firmaData = await firmaRes.json();
          setFirmaNaziv(firmaData.nazivFirme);
          setStanjeRac(firmaData.stanjeRacuna);
          setEmailIzvj(firmaData.emailIzvjestaj);
          setUserId(firmaData.id);
          setFirmaId(firmaData.firmaId);
        }

        if (racuniRes.ok) setListaRacuna(await racuniRes.json());
        if (zaposleniciRes.ok) setListaZaposlenika(await zaposleniciRes.json());
        if (fRes.ok) {
          const FakturData = await fRes.json();
          setF(FakturData.odradjeni);
          setFN(FakturData.neodradjeni);
        }
        if (pnRes.ok) {
          const PNData = await pnRes.json();
          setPutniNalozi(PNData.odradjeni);
          setPutniNaloziNeodradjeni(PNData.neodradjeni);
        }

      } catch (err) {
        alert("Greška pri dohvaćanju podataka" + err);
      } finally {
        setLoading(false);
      }
    }

    fetchAll();
  }, []);

  if (loading) { return <p>Učitavanje podataka...</p>; }

  return (
    <KlijentContext.Provider
      value={{
        userId, firmaId, firmaNaziv, stanjeRac, emailIzvj, listaZaposlenika, listaRacuna, PN, PNN, F, FN
      }}
    >
      {children}
    </KlijentContext.Provider>
  );
}

export function useKlijent() {
  return useContext(KlijentContext);
}