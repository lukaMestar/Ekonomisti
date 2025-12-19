package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class FirmaId implements Serializable {

    private Long idFirma;
    private Long idKlijent;

    public FirmaId() {}

    public FirmaId(Long idFirma, Long idKlijent) {
        this.idFirma = idFirma;
        this.idKlijent = idKlijent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FirmaId)) return false;
        FirmaId that = (FirmaId) o;
        return Objects.equals(idFirma, that.idFirma) &&
               Objects.equals(idKlijent, that.idKlijent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFirma, idKlijent);
    }
}
