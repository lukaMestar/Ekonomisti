import { createContext, useContext, useState, useEffect } from "react";
import {useUser } from "../../UserContext.jsx";

const RacunovodaContext = createContext();

export function RacunovodaProvider({ children }) {
  const [klijenti, setKlijenti] = useState([]);
  const [slobodniKlijenti, setSlobodniKlijenti] = useState([]);
  const oznaciOdradjen = (id) => {
  setKlijenti((prev) =>
    prev.map((k) =>
      k.id === id
        ? { ...k, status: k.status === "Odrađen" ? "Neodrađen" : "Odrađen" }
        : k
    )
  );
};

     // MOCK PODACI
  if (klijenti.length === 0)
      setKlijenti([
        { id: 1, ime: "Klijent A", status: "Neodrađen" },
        { id: 2, ime: "Klijent B", status: "Odrađen" },
        { id: 3, ime: "Klijent C", status: "Neodrađen" },
  ]);

 return (
    <RacunovodaContext.Provider
      value={{tvrtke, setTvrtke, trenutnaTvrtka, setTrenutnaTvrtka }}
    >
      {children}
    </RacunovodaContext.Provider>
  );
}

export function useRacunovoda() {
  return useContext(RacunovodaContext);
}
