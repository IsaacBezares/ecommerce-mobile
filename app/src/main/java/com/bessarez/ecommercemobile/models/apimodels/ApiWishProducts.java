package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiWishProducts {
    @SerializedName("_embedded")
    private ApiWishProducts.Service embedded;

    public Service getEmbedded() {
        return embedded;
    }

    public List<ApiProduct> getEmbeddedServices() {
        return embedded.getWishProducts();
    }

    private class Service {

        @SerializedName("products")
        private List<ApiProduct> wishProductList = new ArrayList<>();

        public List<ApiProduct> getWishProducts() {
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
