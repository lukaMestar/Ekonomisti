
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ZaposlenikRepository extends JpaRepository<Zaposlenik, Long> {
    Optional<Zaposlenik> findByIdKorisnika(Long idKorisnika); // opcionalno, custom metoda
    
    boolean existsByIdKorisnika(Long idKorisnika);

    @Query("""
    SELECT z.placa
    FROM Zaposlenik z
    JOIN z.korisnik k
    WHERE k.idKorisnika = :idKorisnika
    """)
    BigDecimal findPlacaByKorisnikId(
        @Param("idKorisnika") Long idKorisnika
    );
}
