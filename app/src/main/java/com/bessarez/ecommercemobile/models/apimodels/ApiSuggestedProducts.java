package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiSuggestedProducts {

    @SerializedName("_embedded")
    private Service embedded;

    public Service getEmbeddedServices() {
        return embedded;
    }

    public ArrayList<ApiSuggestedProduct> getSuggestionList(){
        return embedded.getApiSuggestedProductList();
    }

    private class Service {

        @SerializedName("products")
        private ArrayList<ApiSuggestedProduct> productSuggestionList = new ArrayList<>();

        public ArrayList<ApiSuggestedProduct> getApiSuggestedProductList() {
            return productSuggestionList;
        }
    }
}
