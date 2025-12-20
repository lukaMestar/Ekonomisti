import { createContext, useContext, useEffect, useState } from "react";
import { API_URL } from "../config.js";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`${API_URL}/role`, {
      credentials: "include"
    })
      .then(res => {
        if (res.status === 401) {
          setUser(null);
          return;
        }
        return res.json();
      })
      .then(data => {
        if (data) setUser(data);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);