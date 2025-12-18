import { useParams } from "react-router-dom";
import { useRacunovoda } from "./RacunovodaContext";
import { useState } from "react";

function PostaviCijenu() {
  const { klijentId } = useParams(); // dohvaća ID iz URL-a
  const { klijenti, postaviCijenu } = useRacunovoda();
  const klijent = klijenti.find(k => k.id === Number(klijentId));

  const [cijena, setCijena] = useState("");

  if (!klijent) return <p>Klijent nije pronađen</p>;

  const handleSpremi = () => {
    const broj = parseFloat(cijena);
    if (isNaN(broj) || broj < 0) {
      alert("Unesite ispravnu brojčanu vrijednost!");
      return;
    }
    postaviCijenu(klijent.id, broj, klijent.ime);
    setCijena("");
  };

  return (
    <div>
      <h1>Postavi cijenu za {klijent.ime}</h1>
      <input
        type="text"
        value={cijena}
        onChange={(e) => setCijena(e.target.value)}
        placeholder="Unesite cijenu"
      />
      <button onClick={handleSpremi}>Spremi cijenu</button>
    </div>
  );
}

export default PostaviCijenu;
