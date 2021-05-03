package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.ProductCategory;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiProductCategories {
    @SerializedName("_embedded")
    private Service embedded;

    public List<ProductCategory> getEmbeddedServices() {
        return embedded.getProductCategories();
    }

    private class Service {

        @SerializedName("productCategories")
        private ArrayList<ProductCategory> productCategoryList = new ArrayList<>();

        public List<ProductCategory> getProductCategories() {
            return productCategoryList;
        }
    }
}
