package com.bessarez.ecommercemobile.ui.models;

public class CardCheckoutItem {
    Long productId;
    int quantity;
    String name;
    String currency;
    Long price;

    public CardCheckoutItem() {
    }

    public CardCheckoutItem(Long productId, int quantity, String name, String currency, Long price) {
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.currency = currency;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}
