import { useState } from "react";
import "./radnik.css";
import { useNavigate } from "react-router-dom";
import { API_URL } from "../../config.js";
import { apiCall } from "../../api.js";
import { useKlijent } from "../KLIJENT/KlijentContext.jsx";

function NoviNalog() {
   const { userId, firmaId } = useKlijent();   
   const Navigate = useNavigate();  
   const [putniNalog, setPutniNalog] = useState({
      polaziste: "",
      odrediste: "",
      datumPolaska: "",
      datumPovratka: "",
      svrhaPutovanja: "",
      prijevoznoSredstvo: "",
      troskoviSmjestaja: "",
      ostaliTroskovi: ""
   });

  const handleSubmit = async (e) => {
   e.preventDefault();
   
   const nalogfin = {
         polaziste: putniNalog.polaziste,
         odrediste: putniNalog.odrediste,
         datumPolaska: putniNalog.datumPolaska,
         datumPovratka: putniNalog.datumPovratka,
         trosak: Number(putniNalog.troskoviSmjestaja) + Number(putniNalog.ostaliTroskovi),
         svrhaPutovanja: putniNalog.svrhaPutovanja,
         prijevoznoSredstvo: putniNalog.prijevoznoSredstvo,
         firma: {
            idFirma: firmaId,
            idKlijent: userId
         },
         zaposlenik: {
            idKorisnika: userId
         },
    };

   try {
         const response = await apiCall(`${API_URL}/api/addnalog`, {
         method: "POST",
         headers: {
            "Content-Type": "application/json",
         },
         body: JSON.stringify(nalogfin),
         });

         const responseText = await response.text();

         if (response.ok) {
            alert("Nalog added successfully");
            Navigate(-1); 
         } else {
            alert("Error adding nalog: " + responseText);
         }
      } catch (error) {
         alert("Error adding nalog: " + error.message);
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
               <label>Odredište:</label>
               <input
                  type="text"
                  value={putniNalog.odrediste}
                  onChange={(e) =>
                     setPutniNalog({ ...putniNalog, odrediste: e.target.value })
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