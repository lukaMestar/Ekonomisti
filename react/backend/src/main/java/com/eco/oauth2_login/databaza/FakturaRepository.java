
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FakturaRepository extends JpaRepository<Faktura, Long> {
    Optional<Faktura> findByIdFaktura(Long idFaktura); // opcionalno, custom metoda

   @Query("""
    SELECT f
    FROM Faktura f 
    WHERE f.firma.idKlijent = :idKlijent
      AND f.firma.idFirma = :idFirma
      AND f.datum BETWEEN :pocetak AND :kraj
    """)
    List<Faktura> findFaktureZaMjesec(
        @Param("idKlijent") Long idKlijent,
        @Param("idFirma") Long idFirma,
        @Param("pocetak") LocalDate pocetak,
        @Param("kraj") LocalDate kraj
    );

    @Query(""" 
        SELECT f 
        FROM Faktura f 
        WHERE f.firma.idKlijent = :idKlijent 
        AND f.firma.idFirma = :idFirma 
        AND f.odradjeno = TRUE 
    """) 
    List<Faktura> findOdradjeneFakture( @Param("idKlijent") Long idKlijent, @Param("idFirma") Long idFirma ); 
    @Query(""" 
        SELECT f 
        FROM Faktura f 
        WHERE f.firma.idKlijent = :idKlijent 
        AND f.firma.idFirma = :idFirma 
    """) 
        List<Faktura> findSveFakture( @Param("idKlijent") Long idKlijent, @Param("idFirma") Long idFirma );
    
}
