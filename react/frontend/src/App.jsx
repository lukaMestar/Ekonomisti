import {
  BrowserRouter as Router,
  Routes,
  Route,
  useSearchParams,
} from "react-router-dom";
import { useEffect } from "react";
import LoginPage from "./pages/loginpage.jsx";
import FrontPage from "./pages/frontpage.jsx";
import Admin from "./USERI/ADMIN/admin.jsx";
import Klijent from "./USERI/KLIJENT/klijent.jsx";
import Racunovoda from "./USERI/RACUNOVODA/racunovoda.jsx";
import Racunklijenti from "./USERI/RACUNOVODA/racunklijenti.jsx";
import Dodajklijenta from "./USERI/RACUNOVODA/dodajklijenta.jsx";
import KlijentInfo from "./USERI/RACUNOVODA/klijentInfo.jsx";
import Izvjestaj from "./USERI/RACUNOVODA/izvjestaj.jsx";
import PostaviCijenu from "./USERI/RACUNOVODA/postaviCijenu.jsx";
import Radnik from "./USERI/RADNIK/radnik.jsx";
import Odabir from "./USERI/RADNIK/odabir.jsx";
import NoviNalog from "./USERI/RADNIK/novinalog.jsx";
import Nalog from "./USERI/RADNIK/nalog.jsx";
import { UserProvider } from "./UserContext.jsx";
import Racuni from "./USERI/KLIJENT/racuni.jsx";
import Podaci from "./USERI/KLIJENT/podaci.jsx";
import DodajKorisnika from "./USERI/ADMIN/DodajKorisnika.jsx";
import Aktivnosti from "./USERI/ADMIN/Aktivnosti.jsx";
import ListaKorisnika from "./USERI/ADMIN/ListaKorisnika.jsx";
import { setSessionToken } from "./api.js";
import { useUser } from "./UserContext.jsx";

function TokenHandler() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { fetchUser } = useUser();

  useEffect(() => {
    const token = searchParams.get("token");
    if (token) {
      console.log("Token found in URL, storing in localStorage");
      setSessionToken(token);
      searchParams.delete("token");
      setSearchParams(searchParams, { replace: true });

      setTimeout(() => {
        fetchUser().catch(() => {});
      }, 100);
    }
  }, [searchParams, setSearchParams, fetchUser]);

  return null;
}

function App() {
  return (
    <UserProvider>
      <Router>
        <TokenHandler />
        <Routes>
          <Route path="/" element={<LoginPage />} />

          <Route path="/pocetna" element={<FrontPage />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/klijent" element={<Klijent />} />
          <Route path="/racunovoda" element={<Racunovoda />} />
          <Route path="/racunklijenti" element={<Racunklijenti />} />
          <Route path="/dodajklijenta" element={<Dodajklijenta />} />
          <Route path="/izvjestaj/:klijentId/:mjesec" element={<Izvjestaj />} />
          <Route path="/klijent/:klijentId" element={<KlijentInfo />} />
          <Route path="/postaviCijenu/:klijentId" element={<PostaviCijenu />} />
          <Route path="/radnik" element={<Radnik />} />
          <Route path="/odabir" element={<Odabir />} />
          <Route path="/nalog" element={<Nalog />} />
          <Route path="/novinalog" element={<NoviNalog />} />
          <Route path="/racuni" element={<Racuni />} />
          <Route path="/podaci" element={<Podaci />} />
          <Route path="/Aktivnosti" element={<Aktivnosti />} />
          <Route path="/ListaKorisnika" element={<ListaKorisnika />} />
          <Route path="/DodajKorisnika" element={<DodajKorisnika />} />
        </Routes>
      </Router>
    </UserProvider>
  );
}

export default App;
