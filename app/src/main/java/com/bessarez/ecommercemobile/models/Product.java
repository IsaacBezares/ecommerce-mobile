package com.bessarez.ecommercemobile.models;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private String ean13;
    private long price;
    private double productWeightKg;
    private String shortDesc;
    private String longDesc;
    private int stock;
    private int quantity;
    private String imageUrl;
    private ProductCategory productCategory;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id,
                   String name,
                   String ean13,
                   long price,
                   double productWeightKg,
                   String shortDesc,
                   String longDesc,
                   int stock,
                   int quantity,
                   String imageUrl,
                   ProductCategory productCategory) {
        this.id = id;
        this.name = name;
        this.ean13 = ean13;
        this.price = price;
        this.productWeightKg = productWeightKg;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.stock = stock;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.productCategory = productCategory;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getProductWeightKg() {
        return productWeightKg;
    }

    public void setProductWeightKg(double productWeightKg) {
        this.productWeightKg = productWeightKg;
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
                ", productWeight=" + productWeightKg +
                ", shortDesc='" + shortDesc + '\'' +
                ", longDesc='" + longDesc + '\'' +
                ", stock=" + stock +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", productCategory=" + productCategory +
//                ", userViewedProducts=" + userViewedProducts +
                '}';
    }
}
