package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OdradjeniPutniNaloziRepository
        extends JpaRepository<OdradjeniPutniNalozi, OdradjeniPutniNaloziId> {

    boolean existsByIdPutniNalogAndIdFirmaAndIdKlijent(
        Long idPutniNalog, Long idFirma, Long idKlijent
    );
}
