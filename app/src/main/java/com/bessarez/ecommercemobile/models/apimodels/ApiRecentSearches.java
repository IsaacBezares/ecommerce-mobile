package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiRecentSearches {
    @SerializedName("_embedded")
    private ApiRecentSearches.Service embedded;

    public ApiRecentSearches.Service getEmbeddedServices() {
        return embedded;
    }

    public ArrayList<ApiRecentSearch> getRecentSearchList(){
        return embedded.getApiRecentSearchList();
    }

    @Override
    public String toString() {
        return  "_embedded : {" + embedded +
                '}';
    }

    private class Service {

        @SerializedName("tupleBackedMaps")
        private ArrayList<ApiRecentSearch> recentSearchList = new ArrayList<>();

        public ArrayList<ApiRecentSearch> getApiRecentSearchList() {
            return recentSearchList;
        }

        @Override
        public String toString() {
            return "tupleBackedMaps{" +
                    "recentSearchList=" + recentSearchList +
                    '}';
        }
    }
}
