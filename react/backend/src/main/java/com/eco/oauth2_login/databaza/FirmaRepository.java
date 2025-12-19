
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FirmaRepository extends JpaRepository<Firma, Long> {
    Optional<Firma> findByIdFirmaAndIdKlijent(Long idFirma, Long idKlijent);
}