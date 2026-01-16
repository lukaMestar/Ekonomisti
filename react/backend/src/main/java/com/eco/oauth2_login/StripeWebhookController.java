package com.eco.oauth2_login;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Stripe Webhook Controller
 * Endpoint: /api/stripe/webhook
 * 
 * Ovo je alternativni endpoint za webhook-e ako želite koristiti /api/stripe/webhook
 * umjesto /api/placanje/webhook
 */
@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {
    
    private final MjesecniRacunService racunService;
    private final StripeService stripeService;
    
    public StripeWebhookController(
        MjesecniRacunService racunService,
        StripeService stripeService
    ) {
        this.racunService = racunService;
        this.stripeService = stripeService;
    }
    
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
