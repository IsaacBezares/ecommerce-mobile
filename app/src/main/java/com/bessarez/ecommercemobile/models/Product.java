package com.bessarez.ecommercemobile.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Product {
    private Long id;
    private String name;
    private String ean13;
    private float price;
    private float productWeight;
    private String shortDesc;
    private String longDesc;
    private int stock;
    private int quantity;
    private String imageUrl;
    private ProductCategory productCategory;
    private Set<UserViewedProduct> userViewedProducts = new HashSet<>();

    public Product() {
    }

    public Product(Long id, String name, String ean13, float price, float productWeight, String shortDesc, String longDesc, int stock, int quantity, String imageUrl, ProductCategory productCategory, Set<UserViewedProduct> userViewedProducts) {
        this.id = id;
        this.name = name;
        this.ean13 = ean13;
        this.price = price;
        this.productWeight = productWeight;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.stock = stock;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.productCategory = productCategory;
        this.userViewedProducts = userViewedProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(float productWeight) {
        this.productWeight = productWeight;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Set<UserViewedProduct> getUserViewedProducts() {
        return userViewedProducts;
    }

    public void setUserViewedProducts(Set<UserViewedProduct> userViewedProducts) {
        this.userViewedProducts = userViewedProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                name.equals(product.name) &&
                ean13.equals(product.ean13);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ean13);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ean13='" + ean13 + '\'' +
                ", price=" + price +
                ", productWeight=" + productWeight +
                ", shortDesc='" + shortDesc + '\'' +
                ", longDesc='" + longDesc + '\'' +
                ", stock=" + stock +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", productCategory=" + productCategory +
                ", userViewedProducts=" + userViewedProducts +
                '}';
    }
}
