package com.bessarez.ecommercemobile.models.apimodels;

public class EphemeralKeyResponse {
    String id;
    String secret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
