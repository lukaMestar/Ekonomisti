import React, { createContext, useContext, useState, useEffect } from "react";
import { KlijentProvider } from "./USERI/KLIJENT/KlijentContext.jsx";
import { RadnikProvider } from "./USERI/RADNIK/RadnikContext.jsx";
import { RacunovodaProvider } from "./USERI/RACUNOVODA/RacunovodaContext.jsx";
import { API_URL } from "./config.js";
import { apiCall, getSessionToken } from "./api.js";

const UserContext = createContext();

function OibModal({ onSpremi }) {
  const [inputOib, setInputOib] = useState("");

  const handleSubmit = () => {
    if (inputOib.length === 11) {
      onSpremi(inputOib);
    } else {
      alert("OIB mora imati točno 11 znamenki!");
    }
  };

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, width: '100%', height: '100%',
      backgroundColor: 'rgba(0,0,0,0.9)', display: 'flex', justifyContent: 'center',
      alignItems: 'center', zIndex: 10000
    }}>
      <div style={{ 
        backgroundColor: 'white', padding: '30px', borderRadius: '12px', 
        textAlign: 'center', boxShadow: '0 4px 15px rgba(0,0,0,0.3)', maxWidth: '400px' 
      }}>
        <h2 style={{ color: '#333' }}>Nedostaje OIB</h2>
        <p style={{ color: '#666' }}>Molimo unesite vaš OIB (11 znamenki) kako biste mogli nastaviti koristiti aplikaciju.</p>
        <input 
          type="text" 
          maxLength="11" 
          value={inputOib} 
          onChange={(e) => setInputOib(e.target.value.replace(/\D/g, ""))}
          placeholder="01234567890"
          style={{ 
            padding: '12px', fontSize: '1.1rem', marginBottom: '20px', 
            width: '100%', borderRadius: '6px', border: '1px solid #ccc' 
          }}
        />
        <button 
          onClick={handleSubmit}
          disabled={inputOib.length !== 11}
          style={{ 
            padding: '12px 25px', fontSize: '1rem', cursor: 'pointer', 
            backgroundColor: inputOib.length === 11 ? '#007bff' : '#ccc', 
            color: 'white', border: 'none', borderRadius: '6px', width: '100%' 
          }}
        >
          Spremi i nastavi
        </button>
      </div>
    </div>
  );
}

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchUser = () => {
    setLoading(true);
    return apiCall(`${API_URL}/api/user`, {
      method: "GET",
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch user");
        return res.json();
      })
      .then((data) => {
        setUser(data);
        setLoading(false);
        return data;
      })
      .catch(() => {
        setLoading(false);
        setUser(null);
        throw new Error("Not authenticated");
      });
  };

  const updateOib = async (noviOib) => {
    try {
      const res = await apiCall(`${API_URL}/api/user/update-oib`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ oib: noviOib }),
      });
      if (res.ok) {
        await fetchUser();
      } else {
        alert("OIB već postoji u sustavu ili je došlo do pogreške.");
      }
    } catch (err) {
      console.error("Greška kod slanja OIB-a:", err);
    }
  };

  useEffect(() => {
    const token = getSessionToken();
    const urlParams = new URLSearchParams(window.location.search);
    const urlToken = urlParams.get("token");

    if (urlToken) {
      setLoading(false);
    } else if (token) {
      fetchUser().catch(() => {});
    } else {
      setLoading(false);
    }
  }, []);

  const contextValue = { user, setUser, fetchUser, updateOib };

  if (loading) return <p>Loading user...</p>;

  if (!user) {
    return (
      <UserContext.Provider value={contextValue}>
        {children}
      </UserContext.Provider>
    );
  }

  return (
    <UserContext.Provider value={contextValue}>
      {user && !user.oib && <OibModal onSpremi={updateOib} />}

      {user.role === "KLIJENT" ? (
        <KlijentProvider user={user}>{children}</KlijentProvider>
      ) : user.role === "RADNIK" ? (

        <RadnikProvider user={user}>{children}</RadnikProvider>
      ) : user.role === "RACUNOVODA" ? (
        <RacunovodaProvider user={user}>{children}</RacunovodaProvider>
      ) : (
        children
      )}
    </UserContext.Provider>
  );
}

export function useUser() {
  return useContext(UserContext);
}