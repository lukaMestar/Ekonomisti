import { useState } from "react";
import "./radnik.css";
import { useNavigate } from "react-router-dom";

function NoviNalog() {


   const Navigate = useNavigate();  
   const [putniNalog, setPutniNalog] = useState({
      polaziste: "",
      destinacija: "",
      datumPolaska: "",
      datumPovratka: "",
      svrhaPutovanja: "",
      prijevoznoSredstvo: "",
      troskoviSmjestaja: "",
      ostaliTroskovi: ""
   });

  const handleSubmit = async (e) => {
  e.preventDefault();
  
  try {
    /* const response = await fetch("#", { // dodati pravi endpoint
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(putniNalog)
    });

    if (!response.ok) throw new Error("Failed to save travel order");

    const result = await response.json();
    console.log("Putni nalog spremljen:", result);

    alert("Putni nalog je uspješno spremljen!");
    
    setPutniNalog({
      polaziste: "",
      destinacija: "",
      datumPolaska: "",
      datumPovratka: "",
      svrhaPutovanja: "",
      prijevoznoSredstvo: "",
      troskoviSmjestaja: "",
      ostaliTroskovi: ""
    }); */

    Navigate(-1);

   }    catch (error) {
         /* console.error("Greška pri spremanju putnog naloga:", error);
         alert("Došlo je do greške pri spremanju putnog naloga!"); */
         Navigate(-1);
      }
   };

   return (
      <div className="nalog-container">
         <h1>Putni nalog</h1>
         
         <form onSubmit={handleSubmit} className="nalog-form">
            <div className="form-group">
               <label>Polazište:</label>
               <input
                  type="text"
                  value={putniNalog.polaziste}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, polaziste: e.target.value })
                  }
                  required
               />
            </div>

            <div className="form-group">
               <label>Destinacija:</label>
               <input
                  type="text"
                  value={putniNalog.destinacija}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, destinacija: e.target.value })
                  }
                  required
               />
            </div>

            <div className="form-group">
               <label>Datum polaska:</label>
               <input
                  type="date"
                  value={putniNalog.datumPolaska}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, datumPolaska: e.target.value })
                  }
                  required
               />
            </div>

            <div className="form-group">
               <label>Datum povratka:</label>
               <input
                  type="date"
                  value={putniNalog.datumPovratka}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, datumPovratka: e.target.value })
                  }
                  required
               />
            </div>

            <div className="form-group">
               <label>Svrha putovanja:</label>
               <textarea
                  value={putniNalog.svrhaPutovanja}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, svrhaPutovanja: e.target.value })
                  }
                  rows="3"
                  required
               />
            </div>

            <div className="form-group">
               <label>Prijevozno sredstvo:</label>
               <select
                  value={putniNalog.prijevoznoSredstvo}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, prijevoznoSredstvo: e.target.value })
                  }
                  required
               >
                  <option value="">Odaberite prijevozno sredstvo</option>
                  <option value="osobno_vozilo">Osobno vozilo</option>
                  <option value="sluzbeno_vozilo">Službeno vozilo</option>
                  <option value="javni_prijevoz">Javni prijevoz</option>
                  <option value="avion">Avion</option>
                  <option value="vlak">Vlak</option>
                  <option value="ostalo">Ostalo</option>
               </select>
            </div>

            <div className="form-group">
               <label>Troškovi smještaja (€):</label>
               <input
                  type="number"
                  value={putniNalog.troskoviSmjestaja}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, troskoviSmjestaja: e.target.value })
                  }
                  min="0"
                  step="0.01"
               />
            </div>

            <div className="form-group">
               <label>Ostali troškovi (€):</label>
               <input
                  type="number"
                  value={putniNalog.ostaliTroskovi}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, ostaliTroskovi: e.target.value })
                  }
                  min="0"
                  step="0.01"
               />
            </div>

            <button type="submit" className="submit-button">
               Spremi putni nalog
            </button>
         </form>
      </div>
   );
}

export default NoviNalog;