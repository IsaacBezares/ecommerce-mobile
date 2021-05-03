package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiWishProducts {
    @SerializedName("_embedded")
    private ApiWishProducts.Service embedded;

    public List<ApiWishProduct> getEmbeddedServices() {
        return embedded.getWishProducts();
    }

    private class Service {

        @SerializedName("wishProducts")
        private ArrayList<ApiWishProduct> wishProductList = new ArrayList<>();

        public List<ApiWishProduct> getWishProducts() {
            return wishProductList;
        }

        @Override
        public String toString() {
            return "Service{" +
                    "wishProductList=" + wishProductList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ApiWishProducts{" +
                "embedded=" + embedded +
                '}';
    }
}
