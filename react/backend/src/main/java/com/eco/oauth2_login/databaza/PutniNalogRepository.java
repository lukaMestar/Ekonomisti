
package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PutniNalogRepository extends JpaRepository<PutniNalog, Long> {
    Optional<PutniNalog> findByIdPutniNalog(Long idPutniNalog); // opcionalno, custom metoda
}
