import { createContext, useContext, useState, useEffect } from "react";

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

  const postaviCijenu = async (id, novaCijena) => {
  setKlijenti((prev) =>
    prev.map((k) =>
      k.id === id ? { ...k, cijena: novaCijena } : k
    )
  );

  // novo - post za postavljanje cijene
  /*try {
    const response = await fetch(`${API_URL}/api/cijene`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ idKlijenta: id, cijena: novaCijena }),
    });

    if (!response.ok) {
      const text = await response.text();
      alert("Greška pri spremanju cijene na server: " + text);
    }
  } catch (error) {
    alert("Greška pri spajanju na server: " + error.message);
  }*/
};

  // Inicijalizacija mock podataka
  useEffect(() => {
  setKlijenti([
    { id: 1, ime: "Klijent A", status: "Neodrađen", cijena: 0 },
    { id: 2, ime: "Klijent B", status: "Odrađen", cijena: 0 },
    { id: 3, ime: "Klijent C", status: "Neodrađen", cijena: 0 },
  ]);
}, []);


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
