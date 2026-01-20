import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./Tablice.css";
import { API_URL } from "../../config.js";

const ROLE_MAP = {
  ADMIN: 1,
  RACUNOVODA: 2,
  KLIJENT: 3,
  RADNIK: 4,
};

export default function ListaKorisnika() {
  const [korisnici, setKorisnici] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedKorisnik, setSelectedKorisnik] = useState(null);
  const [originalKorisnik, setOriginalKorisnik] = useState(null);
  const [formData, setFormData] = useState({
    email: "",
    imeKorisnik: "",
    prezimeKorisnik: "",
    provider: "",
    providerUserId: "",
    datumRegistracije: "",
    idUloge: 1,
  });

  useEffect(() => {
    fetch(`${API_URL}/api/fetchusers`, { credentials: "include" })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setKorisnici(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("There was an error fetching the data: ", error);
        setError("There was an error fetching the data");
        setLoading(false);
      });
  }, []);

  const handleEdit = (korisnik) => {
    const originalData = { ...korisnik };
    setOriginalKorisnik(originalData);
    setSelectedKorisnik(korisnik);
    setFormData({
      email: korisnik.email || "",
      imeKorisnik: korisnik.imeKorisnik || "",
      prezimeKorisnik: korisnik.prezimeKorisnik || "",
      provider: korisnik.provider || "",
      providerUserId: korisnik.providerUserId || "",
      datumRegistracije: korisnik.datumRegistracije || "",
      idUloge: korisnik.idUloge || 1,
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    if (originalKorisnik) {
      setSelectedKorisnik(originalKorisnik);
      setKorisnici((prev) =>
        prev.map((k) =>
          k.idKorisnika === originalKorisnik.idKorisnika ? originalKorisnik : k
        )
      );
    }
    setIsModalOpen(false);
    setSelectedKorisnik(null);
    setOriginalKorisnik(null);
    setFormData({
      email: "",
      imeKorisnik: "",
      prezimeKorisnik: "",
      provider: "",
      providerUserId: "",
      datumRegistracije: "",
      idUloge: 1,
    });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleRoleChange = (e) => {
    const selectedRole = e.target.value;
    setFormData((prev) => ({
      ...prev,
      idUloge: ROLE_MAP[selectedRole],
    }));
  };

  const handleSave = async () => {
    if (!selectedKorisnik) {
      alert("Nema odabranog korisnika");
      return;
    }

    if (!formData.email || !formData.imeKorisnik || !formData.prezimeKorisnik) {
      alert("Molimo unesite sva obavezna polja (email, ime, prezime)");
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/updateuser/${selectedKorisnik.idKorisnika}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({
            idKorisnika: selectedKorisnik.idKorisnika,
            email: formData.email,
            imeKorisnik: formData.imeKorisnik,
            prezimeKorisnik: formData.prezimeKorisnik,
            provider: formData.provider,
            providerUserId: formData.providerUserId,
            datumRegistracije: formData.datumRegistracije,
            idUloge: formData.idUloge,
          }),
        }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Greška pri ažuriranju korisnika");
      }

      const updatedKorisnik = await response.json();

      setKorisnici((prev) =>
        prev.map((k) =>
          k.idKorisnika === updatedKorisnik.idKorisnika
            ? updatedKorisnik
            : k
        )
      );
      setIsModalOpen(false);
      setSelectedKorisnik(null);
      setOriginalKorisnik(null);
      setFormData({
        email: "",
        imeKorisnik: "",
        prezimeKorisnik: "",
        provider: "",
        providerUserId: "",
        datumRegistracije: "",
        idUloge: 1,
      });

      alert("Korisnik je uspješno ažuriran");
    } catch (error) {
      console.error("Greška pri ažuriranju korisnika:", error);
      alert(error.message || "Greška pri ažuriranju korisnika");
    }
  };

  const handleDelete = async (korisnik) => {
    const roleName = Object.keys(ROLE_MAP).find(
      (key) => ROLE_MAP[key] === korisnik.idUloge
    ) || "korisnik";

    const confirmed = window.confirm(
      `Jeste li sigurni da želite obrisati ${roleName.toLowerCase()} "${korisnik.email}"?`
    );

    if (!confirmed) {
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/deleteuser/${korisnik.idKorisnika}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      const data = await response.text();

      if (!response.ok) {
        throw new Error(data || "Greška pri brisanju korisnika");
      }

      // Ukloni korisnika iz liste
      setKorisnici((prev) =>
        prev.filter((k) => k.idKorisnika !== korisnik.idKorisnika)
      );

      alert("Korisnik je uspješno obrisan");
    } catch (error) {
      console.error("Greška pri brisanju korisnika:", error);
      alert(error.message || "Greška pri brisanju korisnika");
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="tablica-box">
      <h2>Pregled korisnika</h2>

      <div className="back">
        <Link to="/admin">Natrag</Link>
      </div>

      <div className="tablica-toolbar">
        <button className="tablica-btn">Sortiraj po idKorisnik</button>
        <button className="tablica-btn">Sortiraj vremenski</button>
      </div>

      <table className="tablica-table">
        <thead>
          <tr>
            <th>ID korisnika</th>
            <th>E-mail</th>
            <th>Datum/Vrijeme registracije</th>
            <th>ID uloge</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {korisnici.map((korisnik) => (
            <tr key={korisnik.idKorisnika}>
              <td>{korisnik.idKorisnika}</td>
              <td>{korisnik.email}</td>
              <td>{korisnik.datumRegistracije}</td>
              <td>{korisnik.idUloge}</td>
              <td>
                <button
                  className="tablica-btn"
                  style={{ backgroundColor: "blue", color: "white" }}
                  onClick={() => handleEdit(korisnik)}
                >
                  Uredi
                </button>
                <button
                  className="tablica-btn"
                  style={{ backgroundColor: "red", color: "white" }}
                  onClick={() => handleDelete(korisnik)}
                >
                  Izbrisi
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {isModalOpen && selectedKorisnik && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Uredi korisnika</h2>
              <button className="modal-close-btn" onClick={handleCloseModal}>
                ×
              </button>
            </div>
            <div className="modal-body">
              <div className="modal-field">
                <label>
                  <strong>ID korisnika:</strong>
                </label>
                <input
                  type="text"
                  value={selectedKorisnik.idKorisnika}
                  disabled
                  className="modal-input modal-input-disabled"
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>E-mail:</strong>
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  className="modal-input"
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>Ime korisnika:</strong>
                </label>
                <input
                  type="text"
                  name="imeKorisnik"
                  value={formData.imeKorisnik}
                  onChange={handleInputChange}
                  className="modal-input"
                  maxLength={50}
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>Prezime korisnika:</strong>
                </label>
                <input
                  type="text"
                  name="prezimeKorisnik"
                  value={formData.prezimeKorisnik}
                  onChange={handleInputChange}
                  className="modal-input"
                  maxLength={50}
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>Provider:</strong>
                </label>
                <input
                  type="text"
                  name="provider"
                  value={formData.provider}
                  onChange={handleInputChange}
                  className="modal-input"
                  maxLength={255}
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>Provider User ID:</strong>
                </label>
                <input
                  type="text"
                  name="providerUserId"
                  value={formData.providerUserId}
                  onChange={handleInputChange}
                  className="modal-input"
                  maxLength={255}
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>Datum registracije:</strong>
                </label>
                <input
                  type="date"
                  name="datumRegistracije"
                  value={formData.datumRegistracije}
                  onChange={handleInputChange}
                  className="modal-input"
                />
              </div>
              <div className="modal-field">
                <label>
                  <strong>Uloga:</strong>
                </label>
                <select
                  value={Object.keys(ROLE_MAP).find(
                    (key) => ROLE_MAP[key] === formData.idUloge
                  )}
                  onChange={handleRoleChange}
                  className="modal-input"
                >
                  {Object.keys(ROLE_MAP).map((roleName) => (
                    <option key={roleName} value={roleName}>
                      {roleName}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="modal-footer">
              <button
                className="tablica-btn"
                style={{ backgroundColor: "blue", color: "white" }}
                onClick={handleSave}
              >
                Spremi promjene
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
