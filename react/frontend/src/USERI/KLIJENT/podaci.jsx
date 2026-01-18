import React from "react";
import { useKlijent } from "./KlijentContext";
import { useEffect, useState } from "react";


import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";

function Podaci() {
  const { podaci, putniNalozi, placeniRacuni, neplaceniRacuni } = useKlijent();

  const safePutniNalozi = Array.isArray(putniNalozi) ? putniNalozi : [];
  const safePlaceniRacuni = Array.isArray(placeniRacuni) ? placeniRacuni : [];
  const safeNeplaceniRacuni = Array.isArray(neplaceniRacuni) ? neplaceniRacuni : [];

  const [firmaNaziv, setFirmaNaziv] = useState(null);
  const [stanjeRac, setStanjeRac] = useState(null);
  const [emailIzvj, setEmailIzvj] = useState(null);

  const [loading, setLoading] = useState(true); // novo

  useEffect(() => {
    async function fetchFirma() {
      try {
        const firmaResponse = await apiCall(`${API_URL}/api/klijent`);

        if (firmaResponse.ok) {
          const firmaData = await firmaResponse.json();
          console.log(firmaData)
          setFirmaNaziv(firmaData.nazivFirme)
          setStanjeRac(firmaData.stanjeRacuna)
          setEmailIzvj(firmaData.emailIzvjestaj)
        } else {
          const errText = await firmaResponse.text();
          alert("Nije moguće dohvatiti firmu: " + errText);
        }
      } catch (error) {
        alert("Greška pri dohvaćanju firme: " + error.message);
      } finally {
        setLoading(false); // signalizira da je dohvat završen
      }
    }

    fetchFirma();
  }, []);

  if (loading) {
    // prikazuje se dok se ne učitaju podaci
    return <p>Učitavanje podataka o firmi...</p>;
  }


  return (
    <div>
      <h1>Podaci korisnika</h1>

      {podaci ? (
        <div>
          <p><strong>Ime firme/obrta:</strong> {firmaNaziv || "N/A"}</p>
          {/*<p><strong>Ime i prezime vlasnika:</strong> {podac || "N/A"}</p>
          <p><strong>OIB:</strong> {podaci.oib || "N/A"}</p>
          <p><strong>Kontakt broj:</strong> {podaci.kontakt || "N/A"}</p>*/}
          <p><strong>E-mail za izvještaje:</strong> {emailIzvj || "N/A"}</p>

          <h3>Financijsko stanje</h3>
          <p><strong>Trenutačno stanje firme:</strong> {stanjeRac ?? "N/A"} €</p>
          

          <h3>Radnici</h3>
          {podaci.radnici && podaci.radnici.length > 0 ? (
            <table border="1" cellPadding="6" style={{ borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th>Ime i prezime</th>
                  <th>Plaća (€)</th>
                  <th>Status zaposlenja</th>
                </tr>
              </thead>
              <tbody>
                {podaci.radnici.map((radnik) => (
                  <tr key={radnik.id}>
                    <td>{radnik.ime}</td>
                    <td>{radnik.placa}</td>
                    <td style={{ color: radnik.aktivan ? "green" : "gray" }}>
                      {radnik.aktivan ? "Zaposlen" : "Nezaposlen"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>Nema unesenih radnika.</p>
          )}
        </div>
      ) : (
        <p>Učitavanje podataka o korisniku...</p>
      )}

      <h2>Putni nalozi i fakture</h2>

      <h3>Putni nalozi</h3>
      {safePutniNalozi.length === 0 ? (
        <p>Nema unesenih putnih naloga.</p>
      ) : (
        <ul>
          {safePutniNalozi.map((nalozi) => (
            <li key={nalozi.id}>
              {nalozi.opis || "Putni nalog"} — {nalozi.datum || "Nepoznato"}
            </li>
          ))}
        </ul>
      )}

      <h3>Neplaćeni računi</h3>
      {safeNeplaceniRacuni.length === 0 ? (
        <p>Nema neplaćenih računa.</p>
      ) : (
        <ul>
          {safeNeplaceniRacuni.map((r) => (
            <li key={r.id}>
              {r.naziv} — {r.iznos} € (dospijeće: {r.datum})
            </li>
          ))}
        </ul>
      )}

      <h3>Plaćeni računi</h3>
      {safePlaceniRacuni.length === 0 ? (
        <p>Nema plaćenih računa.</p>
      ) : (
        <ul>
          {safePlaceniRacuni.map((r) => (
            <li key={r.id}>
              {r.naziv} — {r.iznos} € (plaćeno: {r.datum})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Podaci;
