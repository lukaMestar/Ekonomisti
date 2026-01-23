package com.eco.oauth2_login.report;
import java.time.YearMonth;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.FirmaRepository;


@Component
public class MonthlyReportJob {

    private final FirmaRepository firmaRepository;
    private final ReportService reportService;
    private final ReportMailService mailService;

    public MonthlyReportJob(FirmaRepository firmaRepository, ReportService reportService, ReportMailService mailService) {
        this.firmaRepository = firmaRepository;
        this.reportService = reportService;
        this.mailService = mailService;
    }



    //@Scheduled(cron = "0 0 8 1 * ?")
    @Scheduled(cron = "*/50 * * * * ?") //- za testiranje salje se svako 30sek
    public void generateAndSendReports() {
        //YearMonth mjesec = YearMonth.now().minusMonths(1);
        YearMonth mjesec = YearMonth.now();

        List<Firma> firme = firmaRepository.findAll();

        for (Firma firma : firme) {
            try {
                byte[] pdfIzvjestaj = reportService.generirajMjesecniIzvjestaj( firma.getIdKlijent(),mjesec);
                byte[] pdfRacun = reportService.generirajRacun( firma.getIdKlijent());
                
                mailService.sendReport(firma,pdfIzvjestaj,mjesec);
                reportService.savePdfToStorage(pdfIzvjestaj, firma.getIdFirma(), mjesec);
                mailService.sendReportRacun(firma, pdfRacun, mjesec);
                reportService.savePdfToStorageRacun(pdfIzvjestaj, firma.getIdFirma(), mjesec);

            } catch (Exception e) {
                System.err.println("greska:  " + firma.getIdFirma() + ": " + e.getMessage());
            }
        }
    }
}
