import { createContext, useContext, useState, useEffect } from "react";

const KlijentContext = createContext();

export function KlijentProvider({ children }) {
  const [putniNalozi, setPutniNalozi] = useState([]);
  const [placeniRacuni, setPlaceniRacuni] = useState([]);
   const [neplaceniRacuni, setNeplaceniRacuni] = useState([]);
   const [podaci, setPodaci] = useState({});

   useEffect(() => {
    fetch("http://localhost:9090/api/podaci", {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setPodaci(data))
      .catch((err) => console.error("Greška kod dohvaćanja podataka:", err));
  }, []);



  useEffect(() => {
    fetch("http://localhost:9090/api/placeniRacuni", {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setPlaceniRacuni(data))
      .catch((err) => console.error("Greška kod dohvaćanja placenih racuna:", err));
  }, []);

  useEffect(() => {
    fetch("http://localhost:9090/api/neplaceniRacuni", {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setNeplaceniRacuni(data))
      .catch((err) => console.error("Greška kod dohvaćanja neplacenih racuna:", err));
  }, []);


  useEffect(() => {
    fetch("http://localhost:9090/api/putniNalozi", {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setPutniNalozi(data))
      .catch((err) => console.error("Greška kod dohvaćanja naloga:", err));
  }, []);


    useEffect(() => {
    const mockRacuni = [
      { id: 1, naziv: "Račun 1", iznos: 25.6, datum: "2025-10-10", placen: false },
      { id: 2, naziv: "Račun 2", iznos: 75.2, datum: "2025-10-15", placen: true },
      { id: 3, naziv: "Račun 3", iznos: 33.5, datum: "2025-10-20", placen: false },
      { id: 4, naziv: "Račun 4", iznos: 42.8, datum: "2025-09-30", placen: true },
    ];
    setPlaceniRacuni(mockRacuni.filter(r => r.placen));
    setNeplaceniRacuni(mockRacuni.filter(r => !r.placen));
  }, []);

  return (
    <KlijentContext.Provider value={{ putniNalozi, setPutniNalozi, placeniRacuni , setPlaceniRacuni, neplaceniRacuni, setNeplaceniRacuni, podaci, setPodaci}}>
      {children}
    </KlijentContext.Provider>
  );
}

export function useKlijent() {
  return useContext(KlijentContext);
}
