package com.eco.oauth2_login.databaza;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JeZaposlenRepository
        extends JpaRepository<JeZaposlen, JeZaposlenId> {

    @Query("""
        SELECT f
        FROM JeZaposlen j
        JOIN Firma f
          ON f.idFirma = j.idFirma
         AND f.idKlijent = j.idKlijent
        WHERE j.idZaposlenik = :idZaposlenik
          AND j.statusAktivnosti = true
    """)
    List<Firma> findFirmeZaZaposlenika(@Param("idZaposlenik") Long idZaposlenik);

        @Query("""
        SELECT j.zaposlenik
        FROM JeZaposlen j
        WHERE j.idFirma = :idFirma
          AND j.idKlijent = :idKlijent
          AND j.statusAktivnosti = true
    """)
    List<Zaposlenik> findPopisZaposlenikaZaFirmu(
        @Param("idFirma") Long idFirma,
        @Param("idKlijent") Long idKlijent
    );


}
