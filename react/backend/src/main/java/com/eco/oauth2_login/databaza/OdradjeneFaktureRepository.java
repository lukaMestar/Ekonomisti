package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OdradjeneFaktureRepository
        extends JpaRepository<OdradjeneFakture, OdradjeneFaktureId> {

    boolean existsByIdFakturaAndIdFirmaAndIdKlijent(
        Long idFaktura, Long idFirma, Long idKlijent
    );
}
