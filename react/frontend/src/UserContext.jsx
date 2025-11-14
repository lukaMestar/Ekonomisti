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
    const token = localStorage.getItem("jwt_token");
    if (!token) {
      console.log("UserContext: No JWT token found");
      setUser(null);
      setLoading(false);
      return;
    }

    console.log("UserContext: Fetching user from", `${API_URL}/api/user`);
    fetch(`${API_URL}/api/user`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        console.log("UserContext: Response status", res.status, res.statusText);
        if (!res.ok) {
          // Token invalid, remove it
          localStorage.removeItem("jwt_token");
          console.log("UserContext: User not authenticated");
          setUser(null);
          setLoading(false);
          return;
        }
        return res.json();
      })
      .then((data) => {
        console.log("UserContext: User data received", data);
        if (data) {
          setUser(data);
        }
        setLoading(false);
      })
      .catch((error) => {
        // User is not authenticated
        console.error("UserContext: Error fetching user", error);
        localStorage.removeItem("jwt_token");
        setUser(null);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading user...</p>;

  // If no user, still render children (for login page and public routes)
  if (!user) {
    return (
      <UserContext.Provider value={{ user, setUser }}>
        {children}
      </UserContext.Provider>
    );
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
