import { createContext, useContext, useState, useEffect } from "react";
import { KlijentProvider } from "./USERI/KLIJENT/KlijentContext.jsx";
import { RadnikProvider } from "./USERI/RADNIK/RadnikContext.jsx";
import { RacunovodaProvider } from "./USERI/RACUNOVODA/RacunovodaContext.jsx";
import { API_URL } from "./config.js";
import { apiCall } from "./api.js";

const UserContext = createContext();

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

  useEffect(() => {
    fetchUser().catch(() => {});
  }, []);

  const contextValue = { user, setUser, fetchUser };

  if (loading) return <p>Loading user...</p>;
  if (!user) {
    return (
      <UserContext.Provider value={contextValue}>
        {children}
      </UserContext.Provider>
    );
  }

  if (user.role === "KLIJENT") {
    return (
      <UserContext.Provider value={contextValue}>
        <KlijentProvider user={user}>{children}</KlijentProvider>
      </UserContext.Provider>
    );
  }

  if (user.role === "RADNIK") {
    return (
      <UserContext.Provider value={contextValue}>
        <RadnikProvider user={user}>{children}</RadnikProvider>
      </UserContext.Provider>
    );
  }

  if (user.role === "RACUNOVODA") {
    return (
      <UserContext.Provider value={contextValue}>
        <RacunovodaProvider user={user}>{children}</RacunovodaProvider>
      </UserContext.Provider>
    );
  }

  return (
    <UserContext.Provider value={contextValue}>{children}</UserContext.Provider>
  );
}

export function useUser() {
  return useContext(UserContext);
}
