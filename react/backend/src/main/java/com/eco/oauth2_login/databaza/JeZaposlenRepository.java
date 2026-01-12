package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeZaposlenRepository extends JpaRepository<JeZaposlen, JeZaposlenId> {
    
}
