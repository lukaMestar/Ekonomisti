import { createContext, useContext, useState, useEffect } from "react";
import { API_URL } from "../../config.js";

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

    fetch(`${API_URL}/api/klijenti/${id}/odradjen`, {
      method: "POST",
      credentials: "include",
    }).catch(() => {
      console.warn("Greška pri slanju statusa backendu");
    });
  };

  const postaviCijenu = async (klijentId, cijena) => {
  try {
    const res = await fetch(`${API_URL}/api/cijene`, {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        klijentId,
        cijena,
      }),
    });

    if (!res.ok) throw new Error("Neuspješno spremanje cijene");

    setKlijenti(prev =>
      prev.map(k =>
        k.id === klijentId ? { ...k, cijena } : k
      )
    );
  } catch (err) {
    alert(err.message);
  }
};


  // Inicijalizacija mock podataka - kasnije maknuti
  useEffect(() => {
  setKlijenti([
    { id: 1, ime: "Klijent A", status: "Neodrađen", cijena: 0 },
    { id: 2, ime: "Klijent B", status: "Odrađen", cijena: 0 },
    { id: 3, ime: "Klijent C", status: "Neodrađen", cijena: 0 },
  ]);
    }, []);


// dohvat klijenata/slobodnih klijenata - provjerit adrese
/*
  useEffect(() => {
      fetch(`${API_URL}/api/klijenti`, {
        credentials: "include",
      })
        .then((res) => res.json())
        .then((data) => setKlijenti(data))
        .catch((err) =>
          console.error("Greška kod dohvaćanja klijenata:", err)
        );
    }, []);

    useEffect(() => {
        fetch(`/slobodniKlijenti`, {
          credentials: "include",
        })
          .then((res) => res.json())
          .then((data) => setSlobodniKlijenti(data))
          .catch((err) =>
            console.error("Greška kod dohvaćanja slobodnih klijenata:", err)
          );
      }, []);
*/

  return (
    <RacunovodaContext.Provider
      value={{ klijenti, setKlijenti, slobodniKlijenti, setSlobodniKlijenti, oznaciOdradjen, postaviCijenu }}
    >
      {children}
    </RacunovodaContext.Provider>
  );
}

export function useRacunovoda() {
  return useContext(RacunovodaContext);
}
