package com.eco.oauth2_login;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.FirmaRepository;
import com.eco.oauth2_login.databaza.Klijent;
import com.eco.oauth2_login.databaza.OdradjeneFakture;
import com.eco.oauth2_login.databaza.OdradjeniPutniNalozi;
import com.eco.oauth2_login.databaza.OdradjeniPutniNaloziRepository;
import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.report.FakturaReportService;
import com.eco.oauth2_login.report.FirmaReportService;
import com.eco.oauth2_login.report.OdradjeneFaktureService;
import com.eco.oauth2_login.report.OdradjeniPutniNaloziService;
import com.eco.oauth2_login.report.PutniNalogReportService;

import jakarta.transaction.Transactional;
@Service
public class StatusOdradjenosti {
    private FirmaReportService firmaRepository;
    private FakturaReportService fakturaReportService;
    private PutniNalogReportService putniNalogReportService;
    private OdradjeneFaktureService odradjeneFaktureService;
    private OdradjeniPutniNaloziService odradjeniPutniNaloziService;


    public StatusOdradjenosti(FakturaReportService fakturaReportService, PutniNalogReportService putniNalogReportService,
        FirmaReportService firmaRepository, OdradjeneFaktureService odradjeneFaktureService, OdradjeniPutniNaloziService odradjeniPutniNaloziService) {
        this.firmaRepository = firmaRepository;
        this.fakturaReportService = fakturaReportService;
        this.putniNalogReportService = putniNalogReportService;
        this.odradjeneFaktureService = odradjeneFaktureService;
        this.odradjeniPutniNaloziService = odradjeniPutniNaloziService;

    }

    @Transactional
    public boolean promijeniStatusFaktura(Long klijentid) {
        Firma firma  = firmaRepository.vratiFirmu(klijentid);
        Long firmaid = firma.getIdFirma();

        // 
        
        List<Faktura> neodFakture = fakturaReportService.getNeodradjeneFakture(firmaid);
        if (neodFakture.isEmpty()) {
            return false; // sve fakture odradjene
        }
        for(Faktura f : neodFakture){
            odradjeneFaktureService.oznaciFakturuOdradjenom(f.getIdFaktura(), f.getFirma().getIdFirma(), f.getFirma().getIdKlijent());
        }
        return true; // promjenila sam fakture sve

    }

    @Transactional
    public boolean promijeniStatusPN(Long klijentid) {
        Firma firma  = firmaRepository.vratiFirmu(klijentid);
        Long firmaid = firma.getIdFirma();
        List<PutniNalog> neodFakture = putniNalogReportService.getNeodradjenePutneNaloge(firmaid);
        if (neodFakture.isEmpty()) {
            return false; // sve fakture odradjene
        }

        for(PutniNalog f : neodFakture){
            odradjeniPutniNaloziService.oznaciPutniNalogOdradjenim(f.getIdPutniNalog(), f.getFirma().getIdFirma(), f.getFirma().getIdKlijent());
        }
        return true; // promjenila sam fakture sve

    }

    public boolean trebaAzurirat(Long klijentId) {
        // za svakog klijenta mi vrati firmu
        Firma firma  = firmaRepository.vratiFirmu(klijentId);
        // List<Faktura> odradjeneFakture = odradjeneFaktureService.odrF(firma.getIdFirma());
        // List<PutniNalog> neodFakture = odradjeniPutniNaloziService.odrPN(firma.getIdFirma(), firma.getIdFirma());


        // List<Faktura> fakNE = fakturaReportService.getNeodradjeneFakture(firma.getIdFirma()).isEmpty();
        // List<PutniNalog> pnNe = putniNalogReportService.getNeodradjenePutneNaloge(firma.getIdFirma()).isEmpty();

        boolean fakNE = fakturaReportService.getNeodradjeneFakture(firma.getIdFirma()).isEmpty();
        boolean pnNe = putniNalogReportService.getNeodradjenePutneNaloge(firma.getIdFirma()).isEmpty();
        
        // List<Faktura> fak = fakturaReportService.getSveFakture(firma.getIdFirma(), firma.getIdFirma());
        // List<PutniNalog> pn = putniNalogReportService.getSvePutneNaloge(firma.getIdFirma());
        // Set<Long> odradjeniIds = odradjeneFakture.stream().map(Faktura::getIdFaktura)
        //         .collect(Collectors.toSet());
        // Set<Long> neodredjeniIds = neodFakture.stream().map(Faktura::getIdFaktura)
        //         .collect(Collectors.toSet());

        // if (odradjeniIds.containsAll(neodredjeniIds)){
        //     return "Sve fakture vec odradjene";
        // }


        return !fakNE || !pnNe;
    }





    
}
