package com.eco.oauth2_login.dto;

public class PaymentIntentResponse {
    private String clientSecret;
    private String publishableKey;
    private String paymentIntentId;
    
    public PaymentIntentResponse() {
    }
    
    public PaymentIntentResponse(String clientSecret, String publishableKey, String paymentIntentId) {
        this.clientSecret = clientSecret;
        this.publishableKey = publishableKey;
        this.paymentIntentId = paymentIntentId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public String getPublishableKey() {
        return publishableKey;
    }
    
    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }
    
    public String getPaymentIntentId() {
        return paymentIntentId;
    }
    
    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }
}
