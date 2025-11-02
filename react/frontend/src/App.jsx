import { Auth0Provider } from '@auth0/auth0-react';
import LoginPage from './pages/loginpage.jsx';
import FrontPage from './pages/frontpage.jsx';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

function App() {
  return (
    <Auth0Provider
      domain="#" 
      clientId=""
      authorizationParams={{
        redirect_uri: window.location.origin + "/pocetna"
      }}
      cacheLocation="localstorage"
    >
      <Router>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/pocetna" element={<FrontPage />} />
        </Routes>
      </Router>
    </Auth0Provider>
  );
}

export default App;