package com.bessarez.ecommercemobile.models.apimodels;

public class EphemeralKeyRequest {

    String apiVersion;
    String customerId;

    public EphemeralKeyRequest() {
    }

    public EphemeralKeyRequest(String apiVersion, String customerId) {
        this.apiVersion = apiVersion;
        this.customerId = customerId;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
