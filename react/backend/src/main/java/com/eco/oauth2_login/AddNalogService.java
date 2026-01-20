package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import com.eco.oauth2_login.databaza.*;
import java.time.LocalDate;

@Service
public class AddNalogService {

    private final PutniNalogRepository nalogRepository;
    private final FirmaRepository firmaRepository;
    private final ZaposlenikRepository zaposlenikRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AddNalogService(
            PutniNalogRepository nalogRepository,
            FirmaRepository firmaRepository,
            ZaposlenikRepository zaposlenikRepository,
            ApplicationEventPublisher eventPublisher) {
        this.nalogRepository = nalogRepository;
        this.firmaRepository = firmaRepository;
        this.zaposlenikRepository = zaposlenikRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void addNalog(PutniNalog req) {
        Long idFirma = req.getIdFirma();
        Long idKlijent = req.getIdKlijent();
        Long idZaposlenika = req.getIdZaposlenik();

        if (idFirma == null || idZaposlenika == null) {
            throw new RuntimeException("Missing ID parameters");
        }

        Firma firma = firmaRepository
                .findByIdFirmaAndIdKlijent(idFirma, idKlijent)
                .orElseThrow(() -> new RuntimeException("Firma not found"));

        Zaposlenik zaposlenik = zaposlenikRepository
                .findByIdKorisnika(idZaposlenika)
                .orElseThrow(() -> new RuntimeException("Zaposlenik not found"));

        req.setFirma(firma);
        req.setZaposlenik(zaposlenik);

        if (req.getDatumIzdavanja() == null) {
            req.setDatumIzdavanja(LocalDate.now());
        }

        nalogRepository.save(req);
        eventPublisher.publishEvent(new PutniNalogCreatedEvent(req));
    }
}