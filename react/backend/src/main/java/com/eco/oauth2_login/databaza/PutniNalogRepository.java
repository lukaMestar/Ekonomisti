
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PutniNalogRepository extends JpaRepository<PutniNalog, Long> {
    Optional<PutniNalog> findByIdPutniNalog(Long idPutniNalog); // opcionalno, custom metoda

    @Query("""
    SELECT pn
    FROM PutniNalog pn
    WHERE pn.firma.idFirma = :idFirma
      AND pn.datumPovratka BETWEEN :pocetak AND :kraj
    """)
    List<PutniNalog> findPutniNaloziZaMjesec(
        @Param("idFirma") Long idFirma,
        @Param("pocetak") LocalDate pocetak,
        @Param("kraj") LocalDate kraj
    );

    @Query(""" 
        SELECT pn
        FROM PutniNalog pn
        WHERE pn.firma.idFirma = :idFirma 
    """) 
        List<PutniNalog> findSvePutneNaloge( @Param("idFirma") Long idFirma );

    @Query(""" 
        SELECT pn
        FROM PutniNalog pn
        WHERE pn.firma.idFirma = :idFirma 
        AND pn.zaposlenik.idKorisnika  = :idZaposlenik 
    """) 
        List<PutniNalog> radnikPN( @Param("idFirma") Long idFirma, @Param("idZaposlenik") Long idZaposlenik );
}