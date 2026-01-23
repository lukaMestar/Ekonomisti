package com.eco.oauth2_login.report;

import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.FirmaRepository;
import com.eco.oauth2_login.databaza.Placa;
import com.eco.oauth2_login.databaza.PlacaRepository;
import com.eco.oauth2_login.databaza.PutniNalog;


import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.List;

@Service
public class ReportService {

    private final FakturaReportService FakturaReportService;
    private final PutniNalogReportService putniNalogReportService;
    private final RacunovodaKlijentReportService racunovodaKlijentReportService;
    private final ReportPDFService reportPDFService;
    private final FirmaRepository firmaRepository;
    private final PlacaRepository placaRepository;
    

    public ReportService(FakturaReportService FakturaReportService,PutniNalogReportService putniNalogReportService,
        RacunovodaKlijentReportService racunovodaKlijentReportService,ReportPDFService reportPDFService, FirmaRepository firmaRepository, 
        PlacaRepository placaRepository) {
        this.FakturaReportService = FakturaReportService;
        this.putniNalogReportService = putniNalogReportService;
        this.racunovodaKlijentReportService = racunovodaKlijentReportService;
        this.reportPDFService = reportPDFService;
        this.firmaRepository = firmaRepository;
        this.placaRepository = placaRepository;
    }

    public Path savePdfToStorage(byte[] pdf,Long firmaId,YearMonth mjesec) throws Exception {
        Path dir = Paths.get("/data/reports", firmaId.toString());
        Files.createDirectories(dir);

        String fileName = "izvjestaj_" + mjesec.getMonthValue() + "_" + mjesec.getYear() + ".pdf";
        Path filePath = dir.resolve(fileName);

        Files.write(filePath, pdf);

        return filePath;
    }

    public Path savePdfToStorageRacun(byte[] pdf,Long firmaId,YearMonth mjesec) throws Exception {
        Path dir = Paths.get("/data/reports", firmaId.toString());
        Files.createDirectories(dir);

        String fileName = "racun" + mjesec.getMonthValue() + "_" + mjesec.getYear() + ".pdf";
        Path filePath = dir.resolve(fileName);

        Files.write(filePath, pdf);

        return filePath;
    }

    public byte[] generirajMjesecniIzvjestaj(Long klijentId, YearMonth mjesec) {
        Long firmaId = firmaRepository.findByIdKlijent(klijentId)
                    .orElseThrow(() -> new RuntimeException("Firma ne postoji"))
                    .getIdFirma();

        List<Faktura> fakture = FakturaReportService.getFaktureZaMjesec(klijentId, firmaId, mjesec);

        List<PutniNalog> putniNalozi = putniNalogReportService.getPutniNaloziZaTekuciMjesec(firmaId, mjesec);

        var mjesecniTrosak = racunovodaKlijentReportService.getMjesecniTrosakUsluge(klijentId);
        List<Placa> placaZaposlenika = placaRepository.findByIdFirma(firmaId);

        ReportDTO report = new ReportDTO();
        report.setListaFaktura(fakture);
        report.setListaPutnihNaloga(putniNalozi);
        report.setMjesecniTrosakUsluge(mjesecniTrosak);
        report.setPlacaZaposlenika(placaZaposlenika);
        
        return reportPDFService.generirajIzvjestaj(report);
    }
    public byte[] generirajRacun(Long klijentId) {
        var mjesecniTrosak = racunovodaKlijentReportService.getMjesecniTrosakUsluge(klijentId);

        ReportDTO report = new ReportDTO();
        report.setMjesecniTrosakUsluge(mjesecniTrosak);
        
        return reportPDFService.generirajRacun(report);
    }

    public IzvjestajDTO generirajMjesecniIzvjestajJson(Long klijentID, YearMonth mjesec){
        Long firmaId = firmaRepository.findByIdKlijent(klijentID)
            .orElseThrow(() -> new RuntimeException("Firma ne postoji"))
            .getIdFirma();

        double prihodi = FakturaReportService
                .getFaktureZaMjesec(klijentID, firmaId, mjesec)
                .stream()
                .mapToDouble(f -> f.getIznos().doubleValue())  
                .sum();

        double rashodi = putniNalogReportService
                .getPutniNaloziZaTekuciMjesec(firmaId, mjesec)
                .stream()
                .mapToDouble(pn -> pn.getTrosak().doubleValue())
                .sum();
        var mjesecniTrosak = racunovodaKlijentReportService.getMjesecniTrosakUsluge(klijentID).doubleValue();
        List<Placa> placaZaposlenika = placaRepository.findByIdFirma(firmaId);

        BigDecimal ukupanIznosPlaca = placaZaposlenika.stream()
                                                    .map(Placa::getIznosPlace)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        rashodi = rashodi + mjesecniTrosak + ukupanIznosPlaca.doubleValue();
        double pdv = 0;
        if (prihodi - rashodi > 0)
            pdv = prihodi * 0.25;
        return new IzvjestajDTO(prihodi, rashodi, pdv);
    }
    

}
