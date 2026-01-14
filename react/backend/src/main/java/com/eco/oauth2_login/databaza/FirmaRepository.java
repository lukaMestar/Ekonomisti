
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FirmaRepository extends JpaRepository<Firma, FirmaId> {
    Optional<Firma> findByIdFirmaAndIdKlijent(Long idFirma, Long idKlijent);
    @Query(value = "SELECT nextval('firma_idfirma_seq')", nativeQuery = true)
    Long getNextFirmaId();
}