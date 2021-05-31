package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiSuggestedProducts {

    @SerializedName("_embedded")
    private Service embedded;

    public Service getEmbedded() {
        return embedded;
    }

    public Service getEmbeddedServices() {
        return embedded;
    }

    public ArrayList<Product> getSuggestionList(){
        return embedded.getApiSuggestedProductList();
    }

    private class Service {

        @SerializedName("products")
        private ArrayList<Product> productSuggestionList = new ArrayList<>();

        public ArrayList<Product> getApiSuggestedProductList() {
            return productSuggestionList;
        }
    }
}
