package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.UserViewedProduct;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseApiUserViewedProducts {
    @SerializedName("_embedded")
    private Service embedded;

    public List<UserViewedProduct> getEmbeddedServices() {
        return embedded.getUserViewedProducts();
    }

    private class Service {

        @SerializedName("userViewedProducts")
        private ArrayList<UserViewedProduct> userViewedProductList = new ArrayList<>();

        public List<UserViewedProduct> getUserViewedProducts() {
            return userViewedProductList;
        }
    }
}
