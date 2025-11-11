import { useState } from "react";
import { Link } from "react-router-dom";
import "./Tablice.css";

export default function ListaKorisnika() {
  return (
    <div className="tablica-box">
      <h2>Pregled korisnika</h2>
      
      <div className="back">
        <Link to="/admin">Natrag</Link>
      </div>

      <div className="tablica-toolbar">
        <button className="tablica-btn">Sortiraj po idKorisnik</button>
        <button className="tablica-btn">Sortiraj vremenski</button>
      </div>

      <table className="tablica-table">
        <thead>
          <tr>
            <th>ID korisnika</th>
            <th>E-mail</th>
            <th>Datum/Vrijeme registracije</th>
            <th>ID uloge</th>
          </tr>
        </thead>
        <tbody>{/* namjerno prazno, treba se ispisati podaci iz BD */}</tbody>
      </table>
    </div>
  );
}
