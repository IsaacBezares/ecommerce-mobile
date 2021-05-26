package com.bessarez.ecommercemobile.ui.models;

public class CardCartItem {

    private long id;
    private long productId;
    private String imageUrl;
    private String title;
    private int quantity;
    private double price;
    private int stock;

    public CardCartItem() {
    }

    public CardCartItem(long id, long productId, String imageUrl, String title, int quantity, double price, int stock) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
