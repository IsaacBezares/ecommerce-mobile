package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

public class ApiWishProduct {

    @SerializedName("id")
    private Long id;

    @SerializedName("registeredUser")
    private ApiRegisteredUser registeredUser;

    @SerializedName("product")
    private ApiProduct product;

    public ApiWishProduct() {
    }

    public ApiWishProduct(Long id) {
        this.id = id;
    }

    public ApiWishProduct(ApiRegisteredUser registeredUser, ApiProduct product) {
        this.registeredUser = registeredUser;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiRegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(ApiRegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public ApiProduct getProduct() {
        return product;
    }

    public void setProduct(ApiProduct product) {
        this.product = product;
    }
}
