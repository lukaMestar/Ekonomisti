import { createContext, useContext, useState, useEffect } from "react";
import { API_URL } from "../../config.js";

const RadnikContext = createContext();

export function RadnikProvider({ children, user }) {
  const [tvrtke, setTvrtke] = useState([]);
  const [trenutnaTvrtka, setTrenutnaTvrtka] = useState(null);

  const idKorisnika = user?.id;
  const idFirme = trenutnaTvrtka ? trenutnaTvrtka.idFirma : user?.id_firme;
  const idKlijenta = trenutnaTvrtka ? trenutnaTvrtka.idKlijent : user?.id_klijenta;
  const imeKorisnik = user?.name;
  const prezimeKorisnik = user?.prezime;

  useEffect(() => {
    if (!idKorisnika) return;

    fetch(`${API_URL}/api/tvrtke`, {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`Failed to fetch tvrtke: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        if (data && Array.isArray(data) && data.length > 0) {
          setTvrtke(data);
        } else {
          setTvrtke(["Tvrtka A", "Tvrtka B", "Tvrtka C"]);
        }
      })
      .catch((err) => {
        console.warn("Greška kod dohvaćanja tvrtki:", err.message);
        setTvrtke(["Tvrtka A", "Tvrtka B", "Tvrtka C"]);
      });
  }, [idKorisnika]);

  

  return (
    <RadnikContext.Provider
      value={{ 
        imeKorisnik,
        prezimeKorisnik,
        tvrtke, 
        setTvrtke, 
        trenutnaTvrtka, 
        setTrenutnaTvrtka,
        idFirme,
        idKorisnika,
        idKlijenta
      }}
    >
      {children}
    </RadnikContext.Provider>
  );
}

export function useRadnik() {
  return useContext(RadnikContext);
}