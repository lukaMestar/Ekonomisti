package com.eco.oauth2_login.databaza;

import java.io.Serializable;
import java.util.Objects;

public class RacunovodaKlijentId implements Serializable {

    private Long idRacunovoda;
    private Long idKlijent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RacunovodaKlijentId)) return false;
        RacunovodaKlijentId that = (RacunovodaKlijentId) o;
        return Objects.equals(idRacunovoda, that.idRacunovoda)
                && Objects.equals(idKlijent, that.idKlijent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRacunovoda, idKlijent);
    }
}