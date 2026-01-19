package com.eco.oauth2_login.databaza;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface OdradjeniPutniNaloziRepository
        extends JpaRepository<OdradjeniPutniNalozi, OdradjeniPutniNaloziId> {

    boolean existsByIdPutniNalogAndIdFirmaAndIdKlijent(
        Long idPutniNalog, Long idFirma, Long idKlijent
    );
    @Query("""
        SELECT pn
        FROM OdradjeniPutniNalozi opn
        JOIN opn.putniNalog pn
        WHERE opn.idFirma = :idFirma
    """)
    List<PutniNalog> odradjeniPutniNalozi(@Param("idFirma") Long idFirma);
}
