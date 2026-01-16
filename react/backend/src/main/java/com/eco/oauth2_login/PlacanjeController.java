package com.eco.oauth2_login;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.MjesecniRacun;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.dto.MjesecniRacunDTO;
import com.eco.oauth2_login.dto.MockPaymentResponse;
import com.eco.oauth2_login.dto.PaymentIntentResponse;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/placanje")
@CrossOrigin(origins = {"${FRONTEND_URL:http://localhost:5173}", "http://localhost:5173", "https://ekonomisti.primakon.com"}, allowCredentials = "true")
public class PlacanjeController {
    
    private final MjesecniRacunService racunService;
    private final UserRepository userRepository;
    private final StripeService stripeService;
    
    public PlacanjeController(
        MjesecniRacunService racunService,
        UserRepository userRepository,
        StripeService stripeService
    ) {
        this.racunService = racunService;
        this.userRepository = userRepository;
        this.stripeService = stripeService;
    }
    
    /**
     * Dohvati sve račune za prijavljenog klijenta
     */
    @GetMapping("/racuni")
    public ResponseEntity<List<MjesecniRacunDTO>> getRacuni(
        @AuthenticationPrincipal OAuth2User oauthUser
    ) {
        try {
            String email = oauthUser.getAttribute("email");
            Korisnik korisnik = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
            
            // Provjeri da li je korisnik klijent
            if (korisnik.getIdUloge() == null || korisnik.getIdUloge() != 3) {
                return ResponseEntity.status(403).build();
            }
            
            List<MjesecniRacun> racuni = racunService.getRacuniZaKlijenta(korisnik.getIdKorisnika());
            List<MjesecniRacunDTO> dtos = racuni.stream()
                .map(MjesecniRacunDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Kreiraj Payment Intent za račun (Stripe ili Mock)
     */
    @PostMapping("/create-payment-intent/{racunId}")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(
        @PathVariable Long racunId,
        @AuthenticationPrincipal OAuth2User oauthUser
    ) {
        try {
            String email = oauthUser.getAttribute("email");
            Korisnik korisnik = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
            
            // Provjeri autorizaciju - samo vlasnik računa može platiti
            MjesecniRacun racun = racunService.getRacunById(racunId);
            if (racun == null || !racun.getIdKlijent().equals(korisnik.getIdKorisnika())) {
                return ResponseEntity.status(403).build();
            }
            
            PaymentIntentResponse response = racunService.kreirajPaymentIntent(racunId);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Potvrdi plaćanje nakon uspješnog Stripe plaćanja
     */
    @PostMapping("/confirm-payment/{racunId}")
    public ResponseEntity<PaymentIntentResponse> confirmPayment(
        @PathVariable Long racunId,
        @AuthenticationPrincipal OAuth2User oauthUser
    ) {
        try {
            String email = oauthUser.getAttribute("email");
            Korisnik korisnik = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
            
            MjesecniRacun racun = racunService.getRacunById(racunId);
            if (racun == null || !racun.getIdKlijent().equals(korisnik.getIdKorisnika())) {
                return ResponseEntity.status(403).build();
            }
            
            racunService.oznaciKaoPlaceno(racunId);
            return ResponseEntity.ok(new PaymentIntentResponse("success", "success", "Plaćanje potvrđeno", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Simuliraj plaćanje računa (MOCK - za backward compatibility)
     */
    @PostMapping("/plati/{racunId}")
    public ResponseEntity<MockPaymentResponse> platiRacun(
        @PathVariable Long racunId,
        @AuthenticationPrincipal OAuth2User oauthUser
    ) {
        try {
            String email = oauthUser.getAttribute("email");
            Korisnik korisnik = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
            
            // Provjeri autorizaciju - samo vlasnik računa može platiti
            MjesecniRacun racun = racunService.getRacunById(racunId);
            if (racun == null || !racun.getIdKlijent().equals(korisnik.getIdKorisnika())) {
                return ResponseEntity.status(403).build();
            }
            
            MockPaymentResponse response = racunService.simulirajPlacanje(racunId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Stripe Webhook endpoint za automatsku potvrdu plaćanja
     * URL: /api/placanje/webhook
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
        HttpServletRequest request
    ) {
        String payload;
        String sigHeader = request.getHeader("Stripe-Signature");
        
        // Pročitaj raw body
        try {
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            payload = buffer.toString();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error reading request body");
        }
        
        // Ako Stripe nije konfiguriran, ignoriraj webhook
        if (!stripeService.isStripeEnabled()) {
            return ResponseEntity.ok("Stripe not configured, webhook ignored");
        }
        
        String webhookSecret = stripeService.getWebhookSecret();
        if (webhookSecret == null || webhookSecret.isEmpty()) {
            // Ako nema webhook secret, možemo i dalje procesirati evente (manje sigurno)
            // Ali za produkciju, preporuča se koristiti webhook secret
            return ResponseEntity.status(500).body("Webhook secret not configured");
        }
        
        Event event;
        try {
            // Verificiraj webhook signature
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error processing webhook");
        }
        
        // Procesiraj event
        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElse(null);
            
            if (paymentIntent != null) {
                try {
                    racunService.oznaciKaoPlacenoPoPaymentIntentId(paymentIntent.getId());
                    return ResponseEntity.ok("Payment confirmed");
                } catch (Exception e) {
                    return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
                }
            }
        } else if ("payment_intent.payment_failed".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElse(null);
            
            if (paymentIntent != null) {
                // Loguj grešku, ali ne mijenjaj status računa (ostaje "neplaceno")
                System.out.println("Payment failed for PaymentIntent: " + paymentIntent.getId());
                return ResponseEntity.ok("Payment failure logged");
            }
        }
        
        return ResponseEntity.ok("Event received");
    }
}
