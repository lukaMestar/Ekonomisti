package com.eco.oauth2_login.databaza;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eco.oauth2_login.dto.KlijentDTO;

@Repository
public interface RacunovodaKlijentRepository extends JpaRepository<RacunovodaKlijent, Long> {

    @Query("""
    SELECT new com.eco.oauth2_login.dto.KlijentDTO(
        k.idKorisnika,
        u.imeKorisnik,
        u.prezimeKorisnik
    )
    FROM RacunovodaKlijent rk, Klijent k, Korisnik u
    WHERE rk.idKlijent = k.idKorisnika
      AND k.idKorisnika = u.idKorisnika
      AND rk.idRacunovoda = :racunovodjaId
    """)
    List<KlijentDTO> findKlijentiByRacunovodjaId(
        @Param("racunovodjaId") Long racunovodjaId
    );
    
    Optional<RacunovodaKlijent> findByIdRacunovodaAndIdKlijent(Long idRacunovoda, Long idKlijent);

    @Query("""
    SELECT rk.mjesecniTrosakUsluge
    FROM RacunovodaKlijent rk
    WHERE rk.idKlijent = :klijentId
    """)
    BigDecimal findMjesecniTrosakUslugeByKlijentId(
        @Param("klijentId") Long klijentId
    );

}