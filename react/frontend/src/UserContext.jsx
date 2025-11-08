import { createContext, useContext, useState, useEffect } from "react";
import { KlijentProvider } from "./USERI/KLIJENT/KlijentContext.jsx";
import { RadnikProvider } from "./USERI/RADNIK/RadnikContext.jsx";

const UserContext = createContext();

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);
};


    useEffect(() => {
    fetch("http://localhost:9090/api/user", {
      method: "GET",
      credentials: "include", 
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch user");
        return res.json();
      })
      .then((data) => {
        setUser(data);
        setLoading(false);
      })
      .catch(() => {
        // fallback user
        setUser({ name: "Luka", email: "luka@test.com", role: "RACUNOVODA" });
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading user...</p>;
  if (!user) return <p>No user logged in</p>;

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



   useEffect(() => {
    // Ovdje pozvati API za dohvat svih tvrtki
  });


  useEffect(() => {
      // Ovdje pozvati API za dohvat svih ostalih putnih naloga korisnika
   });

  return (
    <UserContext.Provider
      value={{ user, setUser, tvrtke, setTvrtke, trenutnaTvrtka, setTrenutnaTvrtka, klijenti, setKlijenti, slobodniKlijenti, setSlobodniKlijenti, oznaciOdradjen}}
    >
      {children}
    </UserContext.Provider>
  );


export function useUser() {
  return useContext(UserContext);
}
