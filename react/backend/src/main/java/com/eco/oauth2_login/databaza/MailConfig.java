package com.eco.oauth2_login.databaza;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfig {

   @Value("${BREVO_SMTP_KEY}")
   private String smtpKey;

   @Bean
   public JavaMailSender javaMailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost("smtp-relay.brevo.com");
      mailSender.setPort(2525);

      mailSender.setUsername("a01edd001@smtp-brevo.com");
      // Sada lozinku vučemo iz varijable koju Docker šalje
      mailSender.setPassword(smtpKey);

      Properties props = mailSender.getJavaMailProperties();
      props.put("mail.transport.protocol", "smtp");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.debug", "true");

      return mailSender;
   }
}