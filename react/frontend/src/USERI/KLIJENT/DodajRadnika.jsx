import { useState } from "react";
import { useUser } from "../../UserContext.jsx";

import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";


function DodajRadnika() {
  const [email, setEmail] = useState("");
  const [ime, setIme] = useState("");
  const [placa, setPlaca] = useState("");
  const [idUloge, setRole] = useState(4);
  const { user } = useUser();
  console.log("meow");
  console.log(user);

  const handleSubmit = async (event) => {
    event.preventDefault();

    setIme(event.target.ime.value);
    setEmail(event.target.email.value);
    setPlaca(event.target.placa.value);
    const imeValue = event.target.ime.value.trim();
    const emailValue = event.target.email.value.trim();
    const placaValue = event.target.placa.value.trim();

    const radnikData= {
      ime: imeValue.trim(),
      email: emailValue.trim(),
      firma: "tempFirma", //OVO SE MORA POPRAVIT KAD LOVRINOVIC NAPRAVI SVOJ DIO
      placa: placaValue,
      idUloge,

   };
   const radnikData1= {
      email: emailValue.trim(),
      idUloge,
   }
   const radnikData2 = {
      
   }

    try {
      const response = await apiCall(`${API_URL}/api/adduser`, {
         method: "POST",
         headers: {
            "Content-Type": "application/json",
      },
      body: JSON.stringify(radnikData1),
      });
    
      const responseText = await response.text();
    
      if (response.ok) {
        alert("Korisnik added successfully");
        console.log(responseText)
        const id = parseInt(responseText, 10);
        console.log(id)
        setEmail("");
        const radnikData2 = {
          idKorisnika: id,
          placa: parseFloat(placaValue),

        }
        console.log(radnikData2.idKorisnika)

        const response2 = await apiCall(`${API_URL}/api/addzaposlenik`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(radnikData2),
        });


        //OV
        let firmaId = null;
        try {
          const firmaResponse = await apiCall(`${API_URL}/api/klijent`);
          if (firmaResponse.ok) {
            const firmaData = await firmaResponse.json();
            firmaId = firmaData.firmaId; 
            console.log("Firma ID:", firmaId);
          } else {
            const errText = await firmaResponse.text();
            alert("Nije moguće dohvatiti firmu: " + errText);
            return; // prekini, jer idFirma je potreban
          }
        } catch (error) {
          alert("Greška pri dohvaćanju firme: " + error.message);
          return;
        }
        
         
        const responseText2 = await response2.text();
        if(response2.ok) {
          console.log("uspjelo drugi");
          console.log(id);
          console.log(user.id);
          const radnikData3 = {
            idZaposlenik: id,
            idFirma: firmaId,
            idKlijent: user.id  //user.id STAVI KAD LOVRINOVIC OBAVI SVOJE

          };
          const radnikData4 = {
            idZaposlenik: id,
            idFirma: firmaId,
            iznosPlace: parseFloat(placaValue)
          };
          //Error adding radnik: firmaData is not defined

          const response3 = await apiCall(`${API_URL}/api/addjezaposlen`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(radnikData3),
          });

          const response4 = await apiCall(`${API_URL}/api/addplaca`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(radnikData4),
          });


          const responseText3 = await response3.text();
          const responseText4 = await response4.text();
          if (!(response3.ok && response4.ok)) {
            alert("Treci POST nije uspio: " + responseText3 + responseText4);
          }


        }
        if (!response2.ok) {
          alert("Drugi POST nije uspio: " + responseText2);
        }


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

        <div>
          <label>Placa:</label>
          <input type="number" name="placa" required />
        </div>

        <button type="submit">Dodaj Radnika</button>
      </form>
    </div>
  );
}

export default DodajRadnika;
