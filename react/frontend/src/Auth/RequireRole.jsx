import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

const RequireRole = ({ roles, children }) => {
  const { user, loading } = useAuth();

  if (loading) return null;

  if (!user) {
    return <Navigate to="/" />;
  }

  if (!roles.includes(user.role)) {
    return <Navigate to="/unauthorized" />;
  }

  return children;
};

export default RequireRole;