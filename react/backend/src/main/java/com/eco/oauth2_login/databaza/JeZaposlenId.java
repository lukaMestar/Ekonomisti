package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class JeZaposlenId implements Serializable {

    private Long idZaposlenik;
    private Long idFirma;
    private Long idKlijent;

    public JeZaposlenId() {}

    public JeZaposlenId(Long idZaposlenik, Long idFirma, Long idKlijent) {
        this.idZaposlenik = idZaposlenik;
        this.idFirma = idFirma;
        this.idKlijent = idKlijent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JeZaposlenId)) return false;
        JeZaposlenId that = (JeZaposlenId) o;
        return Objects.equals(idZaposlenik, that.idZaposlenik)
            && Objects.equals(idFirma, that.idFirma)
            && Objects.equals(idKlijent, that.idKlijent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZaposlenik, idFirma, idKlijent);
    }
}
