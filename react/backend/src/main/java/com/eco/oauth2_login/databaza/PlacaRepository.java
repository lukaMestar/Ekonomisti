package com.eco.oauth2_login.databaza;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacaRepository extends JpaRepository<Placa, PlacaId> {

    // Pronalazi sve plaće za određenog zaposlenika
    List<Placa> findByIdZaposlenik(Long idZaposlenik);

    // Pronalazi sve plaće za određenu firmu
    List<Placa> findByIdFirma(Long idFirma);

    @Query("""
    SELECT p FROM Placa p
    WHERE p.idZaposlenik = :z
    AND p.idFirma = :f
    """)
    Placa findPlace(@Param("z") Long z, @Param("f") Long f);


}
