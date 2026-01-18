package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class OdradjeniPutniNaloziId implements Serializable {

    private Long idPutniNalog;
    private Long idFirma;
    private Long idKlijent;

    public OdradjeniPutniNaloziId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OdradjeniPutniNaloziId)) return false;
        OdradjeniPutniNaloziId that = (OdradjeniPutniNaloziId) o;
        return Objects.equals(idPutniNalog, that.idPutniNalog)
            && Objects.equals(idFirma, that.idFirma)
            && Objects.equals(idKlijent, that.idKlijent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPutniNalog, idFirma, idKlijent);
    }
}
