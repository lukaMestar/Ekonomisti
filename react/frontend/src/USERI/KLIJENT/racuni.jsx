import { useKlijent } from "../../USERI/KLIJENT/KlijentContext.jsx";

function Racuni() {
   const { placeniRacuni, neplaceniRacuni } = useKlijent();

   

  const handlePlati = (id) => {
    //placanje
    alert("Plaćanje uspješno izvršeno kreditnom karticom ✅");
  };

  const placeni = placeniRacuni;
  const neplaceni = neplaceniRacuni;

  return (
    <div>
      <h1>Pregled svih računa</h1>


      <h3>Neplaćeni računi</h3>
      {neplaceni.length === 0 ? (
        <p>Nema neplaćenih računa ✅</p>
      ) : (
        <ul>
          {neplaceni.map((r) => (
            <li key={r.id}>
              <strong>{r.naziv}</strong> — {r.iznos} € (dospeće: {r.datum}){" "}
              <button onClick={() => handlePlati(r.id)}>Plati račun 💳</button>
            </li>
          ))}
        </ul>
      )}

      <h3>Svi plaćeni računi</h3>
      {placeni.length === 0 ? (
        <p>Nema plaćenih računa još ⏳</p>
      ) : (
        <ul>
          {placeni.map((r) => (
            <li key={r.id}>
              <strong>{r.naziv}</strong> — {r.iznos} € (plaćeno: {r.datum})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Racuni;
