
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZaposlenikRepository extends JpaRepository<Zaposlenik, Long> {
    Optional<Zaposlenik> findByIdKorisnika(Long idKorisnika); // opcionalno, custom metoda
    boolean existsByIdKorisnika(Long idKorisnika);
}
