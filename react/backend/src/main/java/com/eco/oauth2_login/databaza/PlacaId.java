package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class PlacaId implements Serializable {
    private Long idZaposlenik;
    private Long idFirma;

    public PlacaId() {}

    public PlacaId(Long idZaposlenik, Long idFirma) {
        this.idZaposlenik = idZaposlenik;
        this.idFirma = idFirma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlacaId)) return false;
        PlacaId placaId = (PlacaId) o;
        return Objects.equals(idZaposlenik, placaId.idZaposlenik) &&
               Objects.equals(idFirma, placaId.idFirma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZaposlenik, idFirma);
    }
}
