import { useNavigate } from "react-router-dom";
import { useUser } from "../../UserContext";
import { useParams } from "react-router-dom";

function KlijentInfo() {

const navigate = useNavigate();
const { klijenti } = useUser();
const { klijentId } = useParams();
const klijent = klijenti.find(k => k.id === idKlijenta);

return (
    <div>
        <h1>Klijent klijent.ime</h1>

        <h3>Lista slobodnih klijenata</h3>
            <ul>
                {slobodniKlijenti.length > 0 ? (
                slobodniKlijenti.map((k) => <li key={k.id}>{k.ime}</li>)
                ) : (
                <p>Nemate klijenata.</p>
                )}
            </ul>
    </div>
    )
}

export default KlijentInfo;