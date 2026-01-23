package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FirmaRepository extends JpaRepository<Firma, FirmaId> {

    Optional<Firma> findByIdKlijent(Long idKlijent);

    Optional<Firma> findByIdFirmaAndIdKlijent(Long idFirma, Long idKlijent);

    @Query(value = "SELECT nextval('firma_idfirma_seq')", nativeQuery = true)
    Long getNextFirmaId();

    @Query(value = "SELECT f.* FROM Firma f " +
            "JOIN jeZaposlen jz ON f.idFirma = jz.idFirma AND f.idKlijent = jz.idKlijent " +
            "WHERE jz.idZaposlenik = :radnikId AND jz.statusAktivnosti = TRUE LIMIT 1", nativeQuery = true)
    Optional<Firma> findByRadnikId(@Param("radnikId") Long radnikId);
}