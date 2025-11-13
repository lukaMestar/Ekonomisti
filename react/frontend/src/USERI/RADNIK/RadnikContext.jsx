import { createContext, useContext, useState, useEffect } from "react";
import {useUser } from "../../UserContext.jsx";
import { API_URL } from "../../config.js";

const RadnikContext = createContext();

export function RadnikProvider({ children }) {
  const [tvrtke, setTvrtke] = useState([]);
  const [trenutnaTvrtka, setTrenutnaTvrtka] = useState(null);










    useEffect(() => {
    fetch(`${API_URL}/api/tvrtke`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setTvrtke(data))
      .catch((err) => console.error("Greška kod dohvaćanja tvrtki:", err));
   }, []);


     useEffect(() => {
    if (tvrtke.length === 1 && !trenutnaTvrtka) {
      setTrenutnaTvrtka(tvrtke[0]);
    }
  }, [tvrtke, trenutnaTvrtka]);


    useEffect(() => {
    fetch(`${API_URL}/api/putniNalozi`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setPlaceniRacuni(data))
      .catch((err) => console.error("Greška kod dohvaćanja putnih naloga:", err));
   }, []);


     // MOCK PODACI
  useEffect(() => {
    if (!tvrtke || tvrtke.length === 0) setTvrtke(["Tvrtka A", "Tvrtka B", "Tvrtka C"]);
  }, []);


  return (
    <RadnikContext.Provider
      value={{tvrtke, setTvrtke, trenutnaTvrtka, setTrenutnaTvrtka }}
    >
      {children}
    </RadnikContext.Provider>
  );
}

export function useRadnik() {
  return useContext(RadnikContext);
}
