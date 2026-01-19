import React from "react";
import { useKlijent } from "./KlijentContext";
import { useEffect, useState } from "react";


import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";

function Podaci() {
  const klijent = useKlijent();

  if (!klijent) {
    return <p>Učitavanje konteksta...</p>;
  }
    const {firmaNaziv, stanjeRac, emailIzvj, listaZaposlenika, listaRacuna, PN, PNN,  F, FN} = klijent;

  console.log("PN:", PNN);
  console.log("Tuu saammmmmm --------------------------");

  // if (loading) { return <p>Učitavanje podataka...</p>;}
  
  return (
    <div>
      <h1>Podaci korisnika</h1>
        <div>
          <p><strong>Ime firme/obrta:</strong> {firmaNaziv || "N/A"}</p>
          {/*<p><strong>Ime i prezime vlasnika:</strong> {podac || "N/A"}</p>
          <p><strong>OIB:</strong> {podaci.oib || "N/A"}</p>
          <p><strong>Kontakt broj:</strong> {podaci.kontakt || "N/A"}</p>*/}
          <p><strong>E-mail za izvještaje:</strong> {emailIzvj || "N/A"}</p>

          <h3>Financijsko stanje</h3>
          <p><strong>Trenutačno stanje firme:</strong> {stanjeRac ?? "N/A"} €</p> 

       <h3>Radnici</h3>
          {Array.isArray(listaZaposlenika) && listaZaposlenika.length > 0 ? (
            <table border="1" cellPadding="6" style={{ borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th>Ime zaposlenika</th>
                  <th>Plaća (€)</th>
                </tr>
              </thead>
              <tbody>
                {listaZaposlenika.map((z) => (
                  <tr key={z.idKorisnika}>
                    <td>{z.imeZaposlenik}</td>
                    <td>{z.placa ?? "N/A"}</td>
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
      {Array.isArray(PN) && PN.length === 0 ? (
        <p>Nema odrađenih putnih naloga.</p>
      ) : (
        <ul>
          <p> Odradjeni putni nalozi</p>
           {(PN ??[]).map(nalozi => (
            <li key={nalozi.idPutniNalog}>
              {nalozi.destinacija || "Putni nalog"} : {nalozi.trosak || "Nepoznato"}
            </li>
          ))}
        </ul>
          )}
       

    {Array.isArray(PNN) && PNN.length === 0 ? (
        <p>Nema neodrađenih putnih naloga.</p>
      ) : (
        <ul>
          <p> Neodradjeni putni nalozi </p>
           {(PNN ?? []).map(nalozi => (
            <li key={nalozi.idPutniNalog}>
              {nalozi.destinacija || "Putni nalog"} : {nalozi.trosak || "Nepoznato"}
            </li>
          ))}
        </ul>
      )}
       
     
    
      
      <h3>Fakture</h3>
      {Array.isArray(F) && F.length === 0 ? (
        <p>Nema odrađenih faktura.</p>
      ) : (
        <ul>
          <p> Odradjene Fakture</p>
          {(F ??[]).map(nalozi => (
            <li key={nalozi.idFaktura}>
              Datum fakture: {nalozi.datum} :  {nalozi.iznos || "iznos"} € 
            </li>
          ))}
            </ul>
      )}
    
      {Array.isArray(FN) && FN.length === 0 ? (
        <p>Nema neodrađenih faktura.</p>
      ) : (
        <ul>
          <p> Neodradjene Fakture</p>
          {(FN ??[]).map(nalozi => (
            <li key={nalozi.idFaktura}>
              Datum fakture: {nalozi.datum} :  {nalozi.iznos || "iznos"} € 
            </li>
          ))}
        </ul>
      )}

      <h3>Pregled racuna</h3>
      {Array.isArray(listaRacuna) && listaRacuna.length === 0  ? (
        <p>Nema neplaćenih računa.</p>
      ) : (
        <ul>
          {listaRacuna.map((r) => (
            <li key={r.idRacuna}>
              Placanje troskova racunovodji: {r.datumGeneriranja} : {r.iznos} € (statusRacuna: {r.statusPlacanja})
            </li>
          ))}
        </ul>
      )}

    </div>
  );
}

export default Podaci;