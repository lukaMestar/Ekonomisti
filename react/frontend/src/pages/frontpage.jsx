import { useAuth0 } from "@auth0/auth0-react";

function FrontPage() {
  const { isAuthenticated, user, logout } = useAuth0();

  if (!isAuthenticated) {
    return (
      <div className="loading-container">
        <h1 className="error-title">Niste prijavljeni</h1>
        <a href="/" className="link">
          Vratite se na prijavu
        </a>
      </div>
    );
  }

  return (
    <div className="page-background">
      <div className="content-container">
        <div className="header">
          <h1 className="page-title">Dobrodošli!</h1>
          <button
            onClick={() => logout({ returnTo: window.location.origin })}
            className="logout-button"
          >
            Odjava
          </button>
        </div>

        <div className="success-message">
          <h2 className="success-title">
            Uspješno ste prijavljeni!
          </h2>
          <p className="success-text">
            Dobrodošli, <strong>{user?.name || user?.email}</strong>
          </p>
        </div>

        <div className="info-grid">
          <div className="info-card">
            <h3 className="info-title">Korisnički podaci:</h3>
            <p><strong>Ime:</strong> {user?.name}</p>
            <p><strong>Email:</strong> {user?.email}</p>
            <p><strong>Email verifikovan:</strong> {user?.email_verified ? 'Da' : 'Ne'}</p>
          </div>

          <div className="info-card">
            <h3 className="info-title">Auth0 informacije:</h3>
            <p className="user-id"><strong>User ID:</strong> {user?.sub}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default FrontPage;