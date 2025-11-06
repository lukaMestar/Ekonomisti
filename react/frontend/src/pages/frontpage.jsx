import { useEffect, useState } from "react";

function FrontPage() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    fetch("http://localhost:9090/api/user", {
      method: "GET",
      credentials: "include", 
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch user");
        return res.json();
      })
      .then((data) => setUser(data))
      .catch((err) => console.error(err));
  }, []);

  const handleLogout = async () => {
    try {
      await fetch("http://localhost:9090/logout", {
        method: "POST",
        credentials: "include", 
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      });

      window.location.href = "http://localhost:5173";
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  if (!user) return <a href="http://localhost:5173">Niste prijavljeni. Vrati se na početnu stranicu...</a>;

  return (
    <div className="page-background">
      <div className="content-container">
        <div className="header">
          <h1 className="page-title">Dobrodošli!</h1>
          <button onClick={handleLogout} className="logout-button">
            Odjava
          </button>
        </div>

        <div className="success-message">
          <h2 className="success-title">Uspješno ste prijavljeni!</h2>
          <p className="success-text">
            Dobrodošli, <strong>{user.name}</strong>
          </p>
        </div>

        <div className="info-grid">
          <div className="info-card">
            <h3 className="info-title">Korisnički podaci:</h3>
            <p><strong>Ime:</strong> {user.name}</p>
            <p><strong>Email:</strong> {user.email}</p>
          </div>
          
        </div>
      </div>
    </div>
  );
}

export default FrontPage;
