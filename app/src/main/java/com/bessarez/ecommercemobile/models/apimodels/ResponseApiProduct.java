package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

public class ResponseApiProduct {
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("ean13")
    private String ean13;

    @SerializedName("price")
    private float price;

    @SerializedName("productWeight")
    private float productWeight;

    @SerializedName("shortDesc")
    private String shortDesc;

    @SerializedName("longDesc")
    private String longDesc;

    @SerializedName("stock")
    private int stock;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("productCategory")
    private Category productCategory;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEan13() {
        return ean13;
    }

    public float getPrice() {
        return price;
    }

    public float getProductWeight() {
        return productWeight;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public int getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public class Category {

        @SerializedName("id")
        private Long id;

        @SerializedName("category")
        private String category;

        public Long getId() {
            return id;
        }

        public String getCategory() {
            return category;
        }

    }
}
