import React from "react";
import { useKlijent } from "./KlijentContext";

function Podaci() {
  const klijent = useKlijent();

  if (!klijent) return <p>Učitavanje konteksta...</p>;

  const { firmaNaziv, stanjeRac, emailIzvj, listaZaposlenika, listaRacuna, PN, PNN, F, FN } = klijent;

  return (
    <div>
      <h1>Podaci korisnika</h1>
      <div>
        <p><strong>Ime firme/obrta:</strong> {firmaNaziv || "N/A"}</p>
        <p><strong>E-mail za izvještaje:</strong> {emailIzvj || "N/A"}</p>
        <h3>Financijsko stanje</h3>
        <p><strong>Trenutačno stanje firme:</strong> {stanjeRac ?? "N/A"} €</p>

        <h3>Radnici</h3>
        {Array.isArray(listaZaposlenika) && listaZaposlenika.length > 0 ? (
          <table border="1" cellPadding="6" style={{ borderCollapse: "collapse", width: "100%" }}>
            <thead>
              <tr style={{ backgroundColor: "#f2f2f2" }}>
                <th>Ime zaposlenika</th>
                <th>Plaća (€)</th>
              </tr>
            </thead>
            <tbody>
              {listaZaposlenika.map((z) => (
                <tr key={z.idKorisnika}>
                  <td>{z.imeZaposlenik || "Ime nije dostupno"}</td>
                  <td>{z.placa ? `${z.placa} €` : "N/A"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>Nema unesenih radnika.</p>
        )}
      </div>

      <h2>Lista putnih naloga i faktura</h2>
      <h3>Putni nalozi</h3>
      <ul>
        <p><strong>Odrađeni:</strong></p>
        {PN.length > 0 ? PN.map(n => <li key={n.idPutniNalog}>{n.odrediste}: {n.trosak} €</li>) : <li>Nema podataka</li>}
        <p><strong>Neodrađeni:</strong></p>
        {PNN.length > 0 ? PNN.map(n => <li key={n.idPutniNalog}>{n.odrediste}: {n.trosak} €</li>) : <li>Nema podataka</li>}
      </ul>

      <h3>Fakture</h3>
      <ul>
        <p><strong>Odrađene:</strong></p>
        {F.length > 0 ? F.map(f => <li key={f.idFaktura}>{f.datum}: {f.iznos} €</li>) : <li>Nema podataka</li>}
        <p><strong>Neodrađene:</strong></p>
        {FN.length > 0 ? FN.map(f => <li key={f.idFaktura}>{f.datum}: {f.iznos} €</li>) : <li>Nema podataka</li>}
      </ul>

      <h3>Pregled računa</h3>
      {listaRacuna.length > 0 ? (
        <ul>
          {listaRacuna.map((r) => (
            <li key={r.idRacuna}>{r.datumGeneriranja} : {r.iznos} € ({r.statusPlacanja})</li>
          ))}
        </ul>
      ) : <p>Nema računa.</p>}
    </div>
  );
}

export default Podaci;