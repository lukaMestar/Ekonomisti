
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FakturaRepository extends JpaRepository<Faktura, Long> {
    Optional<Faktura> findByIdFaktura(Long idFaktura); // opcionalno, custom metoda
}
