import { useParams } from "react-router-dom";
import { useRacunovoda } from "./RacunovodaContext";
import { useState, useEffect } from "react";
import { API_URL } from "../../config";
function Izvjestaj() {
  const { klijentId, mjesec } = useParams();
  const { klijenti } = useRacunovoda();
  const [izvjestaj, setIzvjestaj] = useState(null);
  const [loading, setLoading] = useState(true);
  const [odabranaGodina, setOdabranaGodina] = useState(new Date().getFullYear());

  useEffect(() => {
    const dohvatiIzvjestaj = async () => {
      try {
        
        setLoading(true);
        const yearMonth = `${odabranaGodina}-${String(mjesec).padStart(2, "0")}`;
        const response = await fetch(`${API_URL}/api/izvjestaj/${klijentId}/${yearMonth}`);
        if (!response.ok) throw new Error("Greška pri dohvaćanju izvještaja");
        const data = await response.json();
        setIzvjestaj(data);
        
        
      } catch (error) {
        alert("Greška: " + error.message);
        setIzvjestaj(null);
      } finally {
        setLoading(false);
      }
    };

    if (klijentId && mjesec) {
      dohvatiIzvjestaj();
    }
  }, [klijentId, mjesec]);

  if (loading) return <div>Učitavanje izvještaja...</div>;
  // if (!izvjestaj) return <div>Izvještaj nije pronađen.</div>;

  const klijent = klijenti.find(k => k.id === parseInt(klijentId));

  return (
    <div>
      <h1>Mjesečni izvještaj o poslovanju</h1>
      <p>Klijent: {klijent.ime || 'Nepoznat'} (ID: {klijentId})</p>
      <p>Mjesec: {mjesec}.</p>
      <p>Prihodi: {izvjestaj.prihodi || 0} €</p>
      <p>Rashodi: {izvjestaj.rashodi || 0} €</p>
      <p>PDV: {izvjestaj.pdv || 0} €</p>
      <p>Dobitak/gubitak: {izvjestaj.dobitak || 0} €</p>
    </div>
  );
}

export default Izvjestaj;
