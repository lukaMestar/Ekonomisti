import { createContext, useContext, useState, useEffect } from "react";
import { KlijentProvider } from "./USERI/KLIJENT/KlijentContext.jsx";
import { RadnikProvider } from "./USERI/RADNIK/RadnikContext.jsx";
import { RacunovodaProvider } from "./USERI/RACUNOVODA/RacunovodaContext.jsx";
import { API_URL } from "./config.js";

const UserContext = createContext();

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`${API_URL}/api/user`, {
      method: "GET",
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          console.error("Failed to fetch user, status:", res.status);
          throw new Error("Failed to fetch user");
        }
        return res.json();
      })
      .then((data) => {
        console.log("User fetched successfully:", data);
        setUser(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching user:", error);
        // User is not authenticated
        setLoading(false);
        setUser(null);
        // Don't redirect here - let the route handle it
      });
  }, []);

  if (loading) return <p>Loading user...</p>;
  if (!user) {
    // Don't redirect here - let individual routes handle authentication
    // This prevents redirect loops
    return <p>No user logged in</p>;
  }

  if (user.role === "KLIJENT") {
    return (
      <UserContext.Provider value={{ user, setUser }}>
        <KlijentProvider user={user}>{children}</KlijentProvider>
      </UserContext.Provider>
    );
  }

  if (user.role === "RADNIK") {
    return (
      <UserContext.Provider value={{ user, setUser }}>
        <RadnikProvider user={user}>{children}</RadnikProvider>
      </UserContext.Provider>
    );
  }

  if (user.role === "RACUNOVODA") {
    return (
      <UserContext.Provider value={{ user, setUser }}>
        <RacunovodaProvider user={user}>{children}</RacunovodaProvider>
      </UserContext.Provider>
    );
  }

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  return useContext(UserContext);
}
