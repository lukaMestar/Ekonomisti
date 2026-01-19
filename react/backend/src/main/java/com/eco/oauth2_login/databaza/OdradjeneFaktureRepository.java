package com.eco.oauth2_login.databaza;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OdradjeneFaktureRepository
        extends JpaRepository<OdradjeneFakture, OdradjeneFaktureId> {

    boolean existsByIdFakturaAndIdFirmaAndIdKlijent(
        Long idFaktura, Long idFirma, Long idKlijent
    );

     @Query("""
        SELECT f
        FROM OdradjeneFakture opn
        JOIN opn.faktura f
        WHERE opn.idFirma = :idFirma
    """)
    List<Faktura> OdradjeneFakture(@Param("idFirma") Long idFirma);
}
