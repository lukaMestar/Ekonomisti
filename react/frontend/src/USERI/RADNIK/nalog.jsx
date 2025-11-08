
import { useNavigate } from "react-router-dom";


function Nalog() {


  const navigate = useNavigate();
  return (
    <div>
      <h1>Pregled putnih naloga</h1>

      <h3>Napravi novi putni nalog</h3>

      <button
        onClick={() =>  navigate("/novinalog")}
        className="primary-button"
      >
        Novi putni nalog
      </button>
      <h3>Lista postojećih putnih naloga</h3>
    </div>
  );
}


export default Nalog;   
