package com.eco.oauth2_login.databaza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MjesecniRacunRepository extends JpaRepository<MjesecniRacun, Long> {
    
    @Query("SELECT mr FROM MjesecniRacun mr WHERE mr.idKlijent = :klijentId ORDER BY mr.godina DESC, mr.mjesec DESC")
    List<MjesecniRacun> findByKlijentId(@Param("klijentId") Long klijentId);
    
    @Query("SELECT mr FROM MjesecniRacun mr WHERE mr.idRacunovodja = :racunovodjaId ORDER BY mr.godina DESC, mr.mjesec DESC")
    List<MjesecniRacun> findByRacunovodjaId(@Param("racunovodjaId") Long racunovodjaId);
    
    Optional<MjesecniRacun> findByMockPaymentId(String mockPaymentId);
    
    Optional<MjesecniRacun> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    @Query("SELECT mr FROM MjesecniRacun mr WHERE mr.statusPlacanja = 'neplaceno' AND mr.datumRoka < :today")
    List<MjesecniRacun> findOverdueInvoices(@Param("today") LocalDate today);
    
    @Query("SELECT mr FROM MjesecniRacun mr WHERE mr.idRacunovodja = :racunovodjaId AND mr.idKlijent = :klijentId AND mr.idFirma = :firmaId AND mr.mjesec = :mjesec AND mr.godina = :godina")
    Optional<MjesecniRacun> findExistingInvoice(
        @Param("racunovodjaId") Long racunovodjaId,
        @Param("klijentId") Long klijentId,
        @Param("firmaId") Long firmaId,
        @Param("mjesec") Integer mjesec,
        @Param("godina") Integer godina
    );
    
    @Query("SELECT mr FROM MjesecniRacun mr WHERE mr.idKlijent = :klijentId AND mr.idRacunovodja = :racunovodjaId ORDER BY mr.godina DESC, mr.mjesec DESC")
    List<MjesecniRacun> findLatestByKlijentAndRacunovodja(@Param("klijentId") Long klijentId, @Param("racunovodjaId") Long racunovodjaId);
}
