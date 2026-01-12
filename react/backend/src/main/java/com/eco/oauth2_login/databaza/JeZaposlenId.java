package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class JeZaposlenId implements Serializable {

    private Long zaposlenik; // idZaposlenik
    private Long idFirma;
    private Long idKlijent;

    public JeZaposlenId() {}

    public JeZaposlenId(Long zaposlenik, Long idFirma, Long idKlijent) {
        this.zaposlenik = zaposlenik;
        this.idFirma = idFirma;
        this.idKlijent = idKlijent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JeZaposlenId)) return false;
        JeZaposlenId that = (JeZaposlenId) o;
        return Objects.equals(zaposlenik, that.zaposlenik) &&
               Objects.equals(idFirma, that.idFirma) &&
               Objects.equals(idKlijent, that.idKlijent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zaposlenik, idFirma, idKlijent);
    }
}
