package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class OdradjeneFaktureId implements Serializable {

    private Long idFaktura;
    private Long idFirma;
    private Long idKlijent;

    public OdradjeneFaktureId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OdradjeneFaktureId)) return false;
        OdradjeneFaktureId that = (OdradjeneFaktureId) o;
        return Objects.equals(idFaktura, that.idFaktura)
            && Objects.equals(idFirma, that.idFirma)
            && Objects.equals(idKlijent, that.idKlijent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFaktura, idFirma, idKlijent);
    }
}
