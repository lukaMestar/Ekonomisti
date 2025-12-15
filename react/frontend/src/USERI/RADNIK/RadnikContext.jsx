import { createContext, useContext, useState, useEffect } from "react";
import { API_URL } from "../../config.js";

const RadnikContext = createContext();

export function RadnikProvider({ children }) {
  const [tvrtke, setTvrtke] = useState([]);
  const [trenutnaTvrtka, setTrenutnaTvrtka] = useState(null);

  useEffect(() => {
    fetch(`${API_URL}/api/tvrtke`, {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch tvrtke");
        return res.json();
      })
      .then((data) => setTvrtke(data))
      .catch(() => {
        setTvrtke(["Tvrtka A", "Tvrtka B", "Tvrtka C"]);
      });
  }, []);

  useEffect(() => {
    if (tvrtke.length === 1 && !trenutnaTvrtka) {
      setTrenutnaTvrtka(tvrtke[0]);
    }
  }, [tvrtke, trenutnaTvrtka]);

  return (
    <RadnikContext.Provider
      value={{ tvrtke, setTvrtke, trenutnaTvrtka, setTrenutnaTvrtka }}
    >
      {children}
    </RadnikContext.Provider>
  );
}

export function useRadnik() {
  return useContext(RadnikContext);
}
