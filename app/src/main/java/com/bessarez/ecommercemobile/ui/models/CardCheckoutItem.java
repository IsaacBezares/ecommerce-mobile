package com.bessarez.ecommercemobile.ui.models;

public class CardCheckoutItem {
    Long id;
    int quantity;
    String name;
    String currency;
    Long price;

    public CardCheckoutItem() {
    }

    public CardCheckoutItem(Long id, int quantity, String name, String currency, Long price) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.currency = currency;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
