import { createContext, useContext, useState, useEffect } from "react";
import { API_URL } from "../../config.js";

const RacunovodaContext = createContext();

export function RacunovodaProvider({ children }) {
  const [klijenti, setKlijenti] = useState([]);
  const [slobodniKlijenti, setSlobodniKlijenti] = useState([]);
  const [trebaAzurirat, setTrebaAzurirat] = useState({});

  // const oznaciOdradjen = (id) => {
  //   setKlijenti((prev) =>
  //     prev.map((k) =>
  //       k.id === id
  //         ? { ...k, status: k.status === "Odrađen" ? "Neodrađen" : "Odrađen" }
  //         : k
  //     )
  //   );

  const oznaciOdradjen = (id) => {
    fetch(`${API_URL}/api/gumbOdradjeno/${id}`, {
      method: "POST",
      credentials: "include",
    })
      .then(() => {
        setTrebaAzurirat((prev) => ({
            ...prev,
            [id]: false,
          }));
        })
      .catch(() => {
        console.warn("Greška pri slanju backendu");
      });
  };

  // useEffect(() => {
  //   klijenti.forEach((k) => {
  //     fetch(`${API_URL}/api/klijenti/${k.id}/treba-azurirat`, {
  //       credentials: "include",
  //     })
  //       .then((res) => res.json())
  //       .then((data) => {
  //         setTrebaAzurirat((prev) => ({
  //           ...prev,
  //           [k.id]: data,
  //         }));
  //       }).catch((err) => console.error(err));
  //   });
  // }, [klijenti]);



  useEffect(() => {
    fetch(`${API_URL}/api/klijenti/treba-azurirat`, {
      credentials: "include",
    })
      .then(res => res.json())
      .then(setTrebaAzurirat)
      .catch(console.error);
  }, []);

console.log(trebaAzurirat);


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
  fetch(`${API_URL}/api/racunovoda/moji-klijenti`, {
    credentials: "include",
  })
    .then(res => {
      if (!res.ok) throw new Error("Ne mogu dohvatiti klijente");
      return res.json();
    })
    .then(data => {
      setKlijenti(
        data.map(k => ({
          id: k.id,
          ime: `${k.ime} ${k.prezime}`,
          cijena: 0
        }))
      );
    })
    .catch(err => console.error(err));
}, []);


// dohvat klijenata/slobodnih klijenata - provjerit adrese
 useEffect(() => {
    fetch(`${API_URL}/api/racunovoda/slobodni-klijenti`, {
      credentials: "include",
    })
      .then(res => {
        if (!res.ok) throw new Error("Ne mogu dohvatiti slobodne klijente");
        return res.json();
      })
      .then(setSlobodniKlijenti)
      .catch(err => console.error(err));
  }, []);

  return (
    <RacunovodaContext.Provider
      value={{ klijenti, setKlijenti, slobodniKlijenti, setSlobodniKlijenti, oznaciOdradjen, postaviCijenu , trebaAzurirat}}
    >
      {children}
    </RacunovodaContext.Provider>
  );
}

export function useRacunovoda() {
  return useContext(RacunovodaContext);
}
