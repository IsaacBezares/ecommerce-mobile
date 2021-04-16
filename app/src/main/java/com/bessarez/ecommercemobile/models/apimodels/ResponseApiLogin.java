package com.bessarez.ecommercemobile.models.apimodels;

public class ResponseApiLogin {


    private String token;

    public ResponseApiLogin() {
    }

    public ResponseApiLogin(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
