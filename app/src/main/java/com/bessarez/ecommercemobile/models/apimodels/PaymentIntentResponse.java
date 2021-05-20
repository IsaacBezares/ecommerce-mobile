package com.bessarez.ecommercemobile.models.apimodels;

public class PaymentIntentResponse {

    String clientSecret;
    String currency;
    Long amount;

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getAmount() {
        return amount;
    }
}
