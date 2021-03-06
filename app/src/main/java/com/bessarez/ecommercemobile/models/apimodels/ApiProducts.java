package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiProducts {
    @SerializedName("_embedded")
    private Service embedded;

    public List<Product> getEmbeddedServices() {
        return embedded.getProducts();
    }

    public Service getEmbedded() {
        return embedded;
    }

    private class Service {

        @SerializedName("products")
        private ArrayList<Product> productList = new ArrayList<>();

        public List<Product> getProducts() {
            return productList;
        }
    }
}