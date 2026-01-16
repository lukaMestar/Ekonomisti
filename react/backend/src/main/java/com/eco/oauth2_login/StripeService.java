package com.eco.oauth2_login;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class StripeService {
    
    @Value("${STRIPE_SECRET_KEY:}")
    private String stripeSecretKey;
    
    @Value("${STRIPE_PUBLISHABLE_KEY:}")
    private String stripePublishableKey;
    
    @Value("${STRIPE_MODE:test}")
    private String stripeMode; // 'test' ili 'live'
    
    @Value("${STRIPE_WEBHOOK_SECRET:}")
    private String stripeWebhookSecret;
    
    @PostConstruct
    public void init() {
        if (stripeSecretKey != null && !stripeSecretKey.isEmpty()) {
            Stripe.apiKey = stripeSecretKey;
        }
    }
    
    /**
     * Kreira Stripe Customer (ili vraća postojećeg)
     */
    public String createOrGetCustomer(String email, String name) throws StripeException {
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            // Ako nema Stripe ključa, koristi mock
            return "mock_customer_" + email.hashCode();
        }
        
        // U test mode-u, možemo kreirati novog customer-a svaki put
        // ili možemo prvo provjeriti da li postoji
        CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(email)
            .setName(name)
            .build();
        
        Customer customer = Customer.create(params);
        return customer.getId();
    }
    
    /**
     * Kreira Payment Intent za plaćanje
     */
    public PaymentIntent createPaymentIntent(
        String customerId,
        BigDecimal amount,
        String currency,
        String description
    ) throws StripeException {
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            // Mock mode - vraća null, service će koristiti mock plaćanje
            return null;
        }
        
        // Konvertuj BigDecimal u long (Stripe koristi cente)
        long amountInCents = amount.multiply(new BigDecimal("100")).longValue();
        
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amountInCents)
            .setCurrency(currency.toLowerCase())
            .setCustomer(customerId)
            .setDescription(description)
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .build();
        
        return PaymentIntent.create(params);
    }
    
    /**
     * Dohvati Payment Intent po ID-u
     */
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            return null;
        }
        return PaymentIntent.retrieve(paymentIntentId);
    }
    
    /**
     * Vraća publishable key za frontend
     */
    public String getPublishableKey() {
        if (stripePublishableKey == null || stripePublishableKey.isEmpty()) {
            return ""; // Frontend će koristiti mock mode
        }
        return stripePublishableKey;
    }
    
    /**
     * Provjeri da li je Stripe aktiviran
     */
    public boolean isStripeEnabled() {
        return stripeSecretKey != null && !stripeSecretKey.isEmpty();
    }
    
    /**
     * Vraća webhook secret za verifikaciju webhook zahtjeva
     */
    public String getWebhookSecret() {
        return stripeWebhookSecret;
    }
}
