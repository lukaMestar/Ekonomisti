package com.eco.oauth2_login.databaza;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.eco.oauth2_login.databaza.PutniNalogCreatedEvent;
import com.eco.oauth2_login.databaza.EmailService;

@Component
public class PutniNalogListener {

   private final EmailService emailService;

   public PutniNalogListener(EmailService emailService) {
      this.emailService = emailService;
   }

   @EventListener
   @Async
   public void handlePutniNalogCreated(PutniNalogCreatedEvent event) {
      String clientEmail = event.getPutniNalog().getFirma().getKlijent().getKorisnik().getEmail();
      String subject = "Novi putni nalog dodan - "
            + event.getPutniNalog().getZaposlenik().getKorisnik().getImeKorisnik();
      String body = "Poštovani, unesen je novi putni nalog za vašu firmu. Provjerite sustav.";

      emailService.sendEmail(clientEmail, subject, body);
   }
}
