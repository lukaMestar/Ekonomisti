package com.eco.oauth2_login.report;

import java.time.YearMonth;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.Firma;

import jakarta.mail.internet.MimeMessage;

@Service 
public class ReportMailService {

    private final JavaMailSender mailSender;

    public ReportMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendReport(Firma firma, byte[] pdf, YearMonth mjesec) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("test.progij@gmail.com");
            helper.setTo(firma.getEmailIzvjestaj());
            helper.setSubject("Mjesečni financijski izvještaj");
            helper.setText(
                "Poštovani,\n\nu privitku vam šaljemo financijski izvještaj za " 
                + mjesec.getMonthValue() + ". mjesec godine "+ mjesec.getYear() + ".\n\nLp",
                false);

            helper.addAttachment("izvjestaj_" + mjesec.getMonthValue() + "_" + mjesec.getYear()+  ".pdf", new ByteArrayResource(pdf));
            mailSender.send(message);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendReportRacun(Firma firma, byte[] pdf, YearMonth mjesec) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("test.progij@gmail.com");
            helper.setTo(firma.getEmailIzvjestaj());
            helper.setSubject("Račun za mjesečne usluge");
            helper.setText(
                "Poštovani,\n\nu privitku vam šaljemo račun za mjesečne usluge za " 
                + mjesec.getMonthValue() + ". mjesec godine "+ mjesec.getYear() + ".\n\nLp",
                false);

            helper.addAttachment("racun_" + mjesec.getMonthValue() + "_" + mjesec.getYear()+  ".pdf", new ByteArrayResource(pdf));
            mailSender.send(message);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    
}
