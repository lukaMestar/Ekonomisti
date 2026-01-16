package com.eco.oauth2_login.dto;

public class PaymentIntentResponse {
    private String clientSecret;
    private String publishableKey;
    private String paymentIntentId;
    private boolean mockMode;
    
    public PaymentIntentResponse() {
    }
    
    // Za Stripe mode
    public PaymentIntentResponse(String clientSecret, String publishableKey, String paymentIntentId) {
        this.clientSecret = clientSecret;
        this.publishableKey = publishableKey;
        this.paymentIntentId = paymentIntentId;
        this.mockMode = false;
    }
    
    // Za mock mode ili jednostavne odgovore
    public PaymentIntentResponse(String paymentIntentId, String clientSecret, String publishableKey, boolean isMock) {
        this.paymentIntentId = paymentIntentId;
        this.clientSecret = clientSecret;
        this.publishableKey = publishableKey;
        this.mockMode = isMock;
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
    
    public boolean isMockMode() {
        return mockMode;
    }
    
    public void setMockMode(boolean mockMode) {
        this.mockMode = mockMode;
    }
}
