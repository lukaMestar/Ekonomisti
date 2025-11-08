import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from './pages/loginpage.jsx';
import FrontPage from './pages/frontpage.jsx';
import Admin from './USERI/ADMIN/admin.jsx';
import Klijent from './USERI/KLIJENT/klijent.jsx';
import Racunovoda from './USERI/RACUNOVODA/racunovoda.jsx';
import Radnik from './USERI/RADNIK/radnik.jsx';
import Odabir from './USERI/RADNIK/odabir.jsx';
import NoviNalog from './USERI/RADNIK/novinalog.jsx';
import Nalog from './USERI/RADNIK/nalog.jsx';
import { UserProvider } from "./UserContext.jsx";
import Racuni from './USERI/KLIJENT/racuni.jsx';
import Podaci from "./USERI/KLIJENT/podaci.jsx";

function App() {
  return (
    <UserProvider>
      <Router>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          
          <Route path="/pocetna" element={<FrontPage />} />
          <Route path="/test" element={<Admin />} />
          <Route path="/klijent" element={<Klijent />} />
          <Route path="/racunovoda" element={<Racunovoda />} />
          <Route path="/radnik" element={<Radnik />} />
          <Route path="/odabir" element={<Odabir />} />
          <Route path="/nalog" element={<Nalog />} />
          <Route path="/novinalog" element={<NoviNalog />} />
          <Route path="/racuni" element={<Racuni />} />
          <Route path="/podaci" element={<Podaci />} />
        </Routes>
      </Router>
    </UserProvider>
  );
}

export default App;
