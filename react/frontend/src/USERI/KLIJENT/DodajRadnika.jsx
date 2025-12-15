import { useState } from "react";

function DodajRadnika() {
  const [email, setEmail] = useState("");
  const [ime, setIme] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();

    setIme(event.target.ime.value);
    setEmail(event.target.email.value);


    const radnikData= {
      ime: ime.trim(),
      email: email.trim(),
   };

    try {
      const response = await fetch(`#`, {
         method: "POST",
         headers: {
            "Content-Type": "application/json",
      },
      body: JSON.stringify(radnikData),
      });
    
      const responseText = await response.text();
    
      if (response.ok) {
         alert("Korisnik added successfully");
         setEmail("");
      } else {
         alert("Error adding radnik: " + responseText);
      }
    } catch (error) {
      alert("Error adding radnik: " + error.message);
   }
  };

  return (
    <div>
      <h1>Dodaj Radnika</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Ime i prezime:</label>
          <input type="text" name="ime" required />
        </div>

        <div>
          <label>Email:</label>
          <input type="email" name="email" required />
        </div>

        <button type="submit">Dodaj Radnika</button>
      </form>
    </div>
  );
}

export default DodajRadnika;
