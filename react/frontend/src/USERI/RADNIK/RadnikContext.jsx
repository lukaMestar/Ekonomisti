import { createContext, useContext, useState, useEffect } from "react";
import { API_URL } from "../../config.js";

const RadnikContext = createContext();

export function RadnikProvider({ children, user }) {
  const [tvrtke, setTvrtke] = useState([]);
  const [trenutnaTvrtka, setTrenutnaTvrtkaState] = useState(() => {
    try {
      const saved = localStorage.getItem("odabranaTvrtka");
      return saved ? JSON.parse(saved) : null;
    } catch (error) {
      return null;
    }
  });

  const setTrenutnaTvrtka = (tvrtka) => {
    setTrenutnaTvrtkaState(tvrtka);
    if (tvrtka) {
      localStorage.setItem("odabranaTvrtka", JSON.stringify(tvrtka));
    } else {
      localStorage.removeItem("odabranaTvrtka");
    }
  };

  const idKorisnika = user?.id;
  const firmaId = trenutnaTvrtka ? trenutnaTvrtka.idFirma : user?.id_firme;
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
          if (trenutnaTvrtka) {
            const postoji = data.find(
              (t) => t.idFirma === trenutnaTvrtka.idFirma && t.idKlijent === trenutnaTvrtka.idKlijent
            );
            if (!postoji) {
              setTrenutnaTvrtka(null);
            }
          }
        } else {
          setTvrtke([]);
        }
      })
      .catch((err) => {
        console.warn("Greška kod dohvaćanja tvrtki:", err.message);
        setTvrtke([]);
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
        firmaId,
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