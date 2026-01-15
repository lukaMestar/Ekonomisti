package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.databaza.PutniNalogRepository;
import com.eco.oauth2_login.databaza.ZaposlenikRepository;
import com.eco.oauth2_login.databaza.FirmaRepository;

import jakarta.transaction.Transactional;
import com.eco.oauth2_login.databaza.PutniNalogRequest;
import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.Zaposlenik;

import java.time.LocalDate;

import org.springframework.context.ApplicationEventPublisher;
import com.eco.oauth2_login.databaza.PutniNalogCreatedEvent;

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
    public void addNalog(PutniNalogRequest req) {

        Firma firma = firmaRepository
                .findByIdFirmaAndIdKlijent(req.getIdFirma(), req.getIdKlijent())
                .orElseThrow(() -> new RuntimeException("Firma ne postoji"));
        Zaposlenik zaposlenik = zaposlenikRepository
                .findByIdKorisnika(req.getIdZaposlenik())
                .orElseThrow(() -> new RuntimeException("Zaposlenik ne postoji"));

        PutniNalog pn = new PutniNalog();
        pn.setPolaziste(req.getPolaziste());
        pn.setDestinacija(req.getDestinacija());
        pn.setDatumPolaska(req.getDatumPolaska());
        pn.setDatumPovratka(req.getDatumPovratka());
        pn.setTrosak(req.getTrosak());
        pn.setFirma(firma);
        pn.setZaposlenik(zaposlenik);

        nalogRepository.save(pn);

        eventPublisher.publishEvent(new PutniNalogCreatedEvent(pn));
    }
}
