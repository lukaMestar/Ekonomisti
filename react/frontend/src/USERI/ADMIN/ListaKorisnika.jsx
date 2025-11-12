import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./Tablice.css";

export default function ListaKorisnika() {
  const [korisnici, setKorisnici] = useState([]); // State to store users
  const [loading, setLoading] = useState(true); // To handle loading state
  const [error, setError] = useState(null); // To handle error state

  // Fetch users from the backend on component mount
  useEffect(() => {
    // Perform the fetch request
    fetch("http://localhost:9090/api/fetchusers", {credentials: "include"}) // Replace with your actual backend URL
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json(); // Parse the JSON response
      })
      .then((data) => {
        setKorisnici(data); // Store the data in the state
        setLoading(false); // Set loading to false after data is fetched
      })
      .catch((error) => {
        console.error("There was an error fetching the data: ", error);
        setError("There was an error fetching the data");
        setLoading(false); // Stop loading even if there is an error
      });
  }, []); // Empty dependency array ensures it runs only once when the component is mounted

  // Render the loading state or an error message if necessary
  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }
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
        <tbody>
          {korisnici.map((korisnik) => (
            <tr key={korisnik.idKorisnika}>
              <td>{korisnik.idKorisnika}</td>
              <td>{korisnik.email}</td>
              <td>{korisnik.datumRegistracije}</td>
              <td>{korisnik.idUloge}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
