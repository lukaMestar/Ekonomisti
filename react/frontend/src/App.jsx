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
import NovaFaktura from "./USERI/KLIJENT/NovaFaktura.jsx";
import Faktura from "./USERI/KLIJENT/Faktura.jsx";
import DodajRadnika from "./USERI/KLIJENT/DodajRadnika.jsx";
import RequireRole from "./Auth/RequireRole"; 

function TokenHandler() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { fetchUser } = useUser();

  useEffect(() => {
    const token = searchParams.get("token");
    if (token) {
      setSessionToken(token);
      searchParams.delete("token");
      setSearchParams(searchParams, { replace: true });

      fetchUser().catch(() => {});
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
          <Route path="/"                             element={                                                       <LoginPage />                     } />
          <Route path="/pocetna"                      element={                                                       <FrontPage />                     } />
          <Route path="/admin"                        element={<RequireRole roles={["ROLE_ADMIN"                ]}>   <Admin />           </RequireRole>} />
          <Route path="/klijent"                      element={<RequireRole roles={["ROLE_KLIJENT"              ]}>   <Klijent />         </RequireRole>} />
          <Route path="/racunovoda"                   element={<RequireRole roles={["ROLE_RACUNOVODA"           ]}>   <Racunovoda />      </RequireRole>} />
          <Route path="/racunklijenti"                element={<RequireRole roles={["ROLE_RACUNOVODA"           ]}>   <Racunklijenti />   </RequireRole>} />
          <Route path="/dodajklijenta"                element={<RequireRole roles={["ROLE_RACUNOVODA"           ]}>   <Dodajklijenta />   </RequireRole>} />
          <Route path="/izvjestaj/:klijentId/:mjesec" element={<RequireRole roles={["ROLE_RACUNOVODA"           ]}>   <Izvjestaj />       </RequireRole>} />
          <Route path="/klijent/:klijentId"           element={<RequireRole roles={["ROLE_RACUNOVODA"           ]}>   <KlijentInfo />     </RequireRole>} />
          <Route path="/postaviCijenu/:klijentId"     element={<RequireRole roles={["ROLE_RACUNOVODA"           ]}>   <PostaviCijenu />   </RequireRole>} />
          <Route path="/radnik"                       element={<RequireRole roles={["ROLE_RADNIK"               ]}>   <Radnik />          </RequireRole>} />
          <Route path="/odabir"                       element={<RequireRole roles={["ROLE_RADNIK"               ]}>   <Odabir />          </RequireRole>} />
          <Route path="/nalog"                        element={<RequireRole roles={["ROLE_RADNIK","ROLE_KLIJENT"]}>   <Nalog />           </RequireRole>} />
          <Route path="/novinalog"                    element={<RequireRole roles={["ROLE_RADNIK","ROLE_KLIJENT"]}>   <NoviNalog />       </RequireRole>} />
          <Route path="/racuni"                       element={<RequireRole roles={["ROLE_KLIJENT"              ]}>   <Racuni />          </RequireRole>} />
          <Route path="/podaci"                       element={<RequireRole roles={["ROLE_KLIJENT"              ]}>   <Podaci />          </RequireRole>} />
          <Route path="/faktura"                      element={<RequireRole roles={["ROLE_KLIJENT"              ]}>   <Faktura />         </RequireRole>} />
          <Route path="/dodajradnika"                 element={<RequireRole roles={["ROLE_KLIJENT"              ]}>   <DodajRadnika />    </RequireRole>} />
          <Route path="/novafaktura"                  element={<RequireRole roles={["ROLE_KLIJENT"              ]}>   <NovaFaktura />     </RequireRole>} />
          <Route path="/Aktivnosti"                   element={<RequireRole roles={["ROLE_ADMIN"                ]}>   <Aktivnosti />      </RequireRole>} />
          <Route path="/ListaKorisnika"               element={<RequireRole roles={["ROLE_ADMIN"                ]}>   <ListaKorisnika />  </RequireRole>} />
          <Route path="/DodajKorisnika"               element={<RequireRole roles={["ROLE_ADMIN"                ]}>   <DodajKorisnika />  </RequireRole>} />
        </Routes>
      </Router>
    </UserProvider>
  );
}

export default App;
