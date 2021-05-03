package com.bessarez.ecommercemobile.models.apimodels;

public class ApiToken {


    private String token;

    public ApiToken() {
    }

    public ApiToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
