package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Korisnik, Long> {
    Optional<Korisnik> findByEmail(String email);
}