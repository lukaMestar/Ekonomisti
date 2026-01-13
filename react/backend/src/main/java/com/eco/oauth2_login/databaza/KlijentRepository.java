package com.eco.oauth2_login.databaza;

import com.eco.oauth2_login.dto.KlijentDTO;
import com.eco.oauth2_login.dto.KlijentProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KlijentRepository extends JpaRepository<Klijent, Long> {

    @Query(
    value = """
        SELECT k.idkorisnika AS id, ko.imekorisnik AS ime, ko.prezimekorisnik AS prezime
        FROM Klijent k
        JOIN Korisnici ko ON ko.idkorisnika = k.idkorisnika
        WHERE NOT EXISTS (
            SELECT 1 FROM racunovodjaklijent rk WHERE rk.idklijent = k.idkorisnika
        )
    """,
    nativeQuery = true
)
List<KlijentDTO> findSlobodniKlijenti();
}