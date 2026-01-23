package com.eco.oauth2_login;

import com.eco.oauth2_login.databaza.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eco.oauth2_login.dto.PaymentIntentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MjesecniRacunService {
    
    private final MjesecniRacunRepository racunRepository;
    private final RacunovodaKlijentRepository rkRepository;
    private final FirmaRepository firmaRepository;
    private final StripeService stripeService;
    private final UserRepository userRepository;
    
    public MjesecniRacunService(
        MjesecniRacunRepository racunRepository,
        RacunovodaKlijentRepository rkRepository,
        FirmaRepository firmaRepository,
        StripeService stripeService,
        UserRepository userRepository
    ) {
        this.racunRepository = racunRepository;
        this.rkRepository = rkRepository;
        this.firmaRepository = firmaRepository;
        this.stripeService = stripeService;
        this.userRepository = userRepository;
    }
    
    /**
     * Generira mjesečne račune za sve aktivne klijente.
     * Pokreće se automatski svaki dan u 2:00 AM.
     * Generira račun za svakog klijenta svakih mjesec dana od datuma registracije (ili zadnjeg računa).
     */
    @Scheduled(cron = "0 0 2 * * ?") // 2:00 AM, svaki dan
    @Transactional
    public void generirajMjesecneRacune() {
        LocalDate today = LocalDate.now();
        
        List<RacunovodaKlijent> aktivneVeze = rkRepository.findAll();
        
        for (RacunovodaKlijent veza : aktivneVeze) {
            if (veza.getMjesecniTrosakUsluge() == null || 
                veza.getMjesecniTrosakUsluge().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            
            Optional<Firma> firmaOpt = firmaRepository.findByIdKlijent(veza.getIdKlijent());
            if (firmaOpt.isEmpty()) {
                continue;
            }
            
            Firma firma = firmaOpt.get();
            
            // Pronađi zadnji račun za ovog klijenta
            List<MjesecniRacun> zadnjiRacuni = racunRepository.findLatestByKlijentAndRacunovodja(
                veza.getIdKlijent(), veza.getIdRacunovoda()
            );
            
            LocalDate referentniDatum;
            
            if (zadnjiRacuni.isEmpty()) {
                // Nema postojećih računa - koristi datum registracije klijenta
                Optional<Korisnik> klijentOpt = userRepository.findById(veza.getIdKlijent());
                if (klijentOpt.isEmpty()) {
                    continue;
                }
                Korisnik klijent = klijentOpt.get();
                referentniDatum = klijent.getDatumRegistracije();
            } else {
                // Koristi datum generiranja zadnjeg računa
                MjesecniRacun zadnjiRacun = zadnjiRacuni.get(0);
                referentniDatum = zadnjiRacun.getDatumGeneriranja();
            }
            
            // Provjeri da li je prošao mjesec dana od referentnog datuma
            if (today.isBefore(referentniDatum.plusMonths(1))) {
                continue; // Još nije prošao mjesec dana
            }
            
            // Generiraj račun za trenutni mjesec
            int mjesec = today.getMonthValue();
            int godina = today.getYear();
            
            // Provjeri da li račun već postoji za ovaj mjesec
            Optional<MjesecniRacun> existing = racunRepository.findExistingInvoice(
                veza.getIdRacunovoda(),
                veza.getIdKlijent(),
                firma.getIdFirma(),
                mjesec,
                godina
            );
            
            if (existing.isPresent()) {
                continue;
            }
            
            MjesecniRacun racun = new MjesecniRacun();
            racun.setIdRacunovodja(veza.getIdRacunovoda());
            racun.setIdKlijent(veza.getIdKlijent());
            racun.setIdFirma(firma.getIdFirma());
            racun.setMjesec(mjesec);
            racun.setGodina(godina);
            racun.setIznos(veza.getMjesecniTrosakUsluge());
            racun.setDatumGeneriranja(today);
            racun.setDatumRoka(today.plusDays(14));
            racun.setStatusPlacanja("neplaceno");
            
            racunRepository.save(racun);
        }
    }
    
    /**
     * Ručno generira račun za klijenta (za trenutni mjesec)
     */
    @Transactional
    public MjesecniRacun generirajRacunZaKlijenta(Long racunovodjaId, Long klijentId) {
        LocalDate today = LocalDate.now();
        int mjesec = today.getMonthValue();
        int godina = today.getYear();
        
        return generirajRacunZaMjesec(racunovodjaId, klijentId, mjesec, godina);
    }
    
    /**
     * Ručno generira račun za određeni mjesec/godinu
     */
    @Transactional
    public MjesecniRacun generirajRacunZaMjesec(
        Long racunovodjaId,
        Long klijentId,
        Integer mjesec,
        Integer godina
    ) {
        Optional<RacunovodaKlijent> veza = rkRepository.findByIdRacunovodaAndIdKlijent(
            racunovodjaId, klijentId
        );
        
        if (veza.isEmpty() || veza.get().getMjesecniTrosakUsluge() == null) {
            throw new RuntimeException("Cijena usluge nije definirana");
        }
        
        Optional<Firma> firmaOpt = firmaRepository.findByIdKlijent(klijentId);
        if (firmaOpt.isEmpty()) {
            throw new RuntimeException("Klijent nema firmu");
        }
        
        Firma firma = firmaOpt.get();
        
        Optional<MjesecniRacun> existing = racunRepository.findExistingInvoice(
            racunovodjaId, klijentId, firma.getIdFirma(), mjesec, godina
        );
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        MjesecniRacun racun = new MjesecniRacun();
        racun.setIdRacunovodja(racunovodjaId);
        racun.setIdKlijent(klijentId);
        racun.setIdFirma(firma.getIdFirma());
        racun.setMjesec(mjesec);
        racun.setGodina(godina);
        racun.setIznos(veza.get().getMjesecniTrosakUsluge());
        racun.setDatumGeneriranja(LocalDate.now());
        racun.setDatumRoka(LocalDate.now().plusDays(14));
        racun.setStatusPlacanja("neplaceno");
        
        return racunRepository.save(racun);
    }
    
    /**
     * Kreira Payment Intent za račun (Stripe)
     */
    @Transactional
    public PaymentIntentResponse kreirajPaymentIntent(Long racunId) throws StripeException {
        MjesecniRacun racun = racunRepository.findById(racunId)
            .orElseThrow(() -> new RuntimeException("Račun nije pronađen"));
        
        if (!"neplaceno".equals(racun.getStatusPlacanja())) {
            throw new RuntimeException("Račun je već obrađen");
        }
        
        // Provjeri da li je Stripe aktiviran
        if (!stripeService.isStripeEnabled()) {
            throw new RuntimeException("Stripe nije konfiguriran. Plaćanje nije moguće.");
        }
        
        // Koristi Stripe
        Korisnik klijent = userRepository.findById(racun.getIdKlijent())
            .orElseThrow(() -> new RuntimeException("Klijent nije pronađen"));
        
        // Kreiraj ili dohvati Stripe Customer
        String customerId = racun.getStripeCustomerId();
        if (customerId == null || customerId.isEmpty()) {
            String customerName = klijent.getImeKorisnik() + " " + klijent.getPrezimeKorisnik();
            customerId = stripeService.createOrGetCustomer(klijent.getEmail(), customerName);
            racun.setStripeCustomerId(customerId);
        }
        
        // Kreiraj Payment Intent
        String description = String.format("Račun za računovodstvo - %d/%d", 
            racun.getMjesec(), racun.getGodina());
        
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(
            customerId,
            racun.getIznos(),
            "eur", // ili "hrk" ovisno o vašoj valuti
            description
        );
        
        if (paymentIntent == null) {
            throw new RuntimeException("Greška pri kreiranju Payment Intent-a");
        }
        
        racun.setStripePaymentIntentId(paymentIntent.getId());
        racunRepository.save(racun);
        
        return new PaymentIntentResponse(
            paymentIntent.getClientSecret(),
            stripeService.getPublishableKey(),
            paymentIntent.getId()
        );
    }
    
    /**
     * Ažurira status računa nakon uspješnog Stripe plaćanja
     */
    @Transactional
    public void oznaciKaoPlaceno(Long racunId) {
        MjesecniRacun racun = racunRepository.findById(racunId)
            .orElseThrow(() -> new RuntimeException("Račun nije pronađen"));
        
        racun.setStatusPlacanja("placeno");
        racun.setDatumPlacanja(LocalDate.now());
        
        // Ažuriraj financijsko stanje firme
        Optional<Firma> firmaOpt = firmaRepository.findByIdFirmaAndIdKlijent(
            racun.getIdFirma(), racun.getIdKlijent()
        );
        
        if (firmaOpt.isPresent()) {
            Firma firma = firmaOpt.get();
            BigDecimal novoStanje = firma.getStanjeRacuna().subtract(racun.getIznos());
            firma.setStanjeRacuna(novoStanje);
            firmaRepository.save(firma);
        }
        
        racunRepository.save(racun);
    }
    
    /**
     * Ažurira status računa nakon uspješnog Stripe plaćanja (preko Payment Intent ID)
     */
    @Transactional
    public void oznaciKaoPlacenoPoPaymentIntentId(String paymentIntentId) {
        Optional<MjesecniRacun> racunOpt = racunRepository.findByStripePaymentIntentId(paymentIntentId);
        
        if (racunOpt.isEmpty()) {
            throw new RuntimeException("Račun nije pronađen za payment intent: " + paymentIntentId);
        }
        
        MjesecniRacun racun = racunOpt.get();
        oznaciKaoPlaceno(racun.getIdRacun());
    }
    
    public List<MjesecniRacun> getRacuniZaKlijenta(Long klijentId) {
        return racunRepository.findByKlijentId(klijentId);
    }
    
    public MjesecniRacun getRacunById(Long racunId) {
        return racunRepository.findById(racunId).orElse(null);
    }
}
