
import { useNavigate } from "react-router-dom";


function Faktura() {


  const navigate = useNavigate();
  return (
    <div>
      <h1>Pregled faktura</h1>

      <h3>Napravi novu fakturu</h3>

      <button
        onClick={() =>  navigate("/novafaktura")}
        className="primary-button"
      >
        Nova faktura
      </button>
      <h3>Lista postojećih faktura</h3>
    </div>
  );
}


export default Faktura;   
