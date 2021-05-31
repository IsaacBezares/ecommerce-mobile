package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.RecentSearch;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiRecentSearches {
    @SerializedName("_embedded")
    private ApiRecentSearches.Service embedded;

    public Service getEmbedded() {
        return embedded;
    }

    public ApiRecentSearches.Service getEmbeddedServices() {
        return embedded;
    }

    public ArrayList<RecentSearch> getRecentSearchList(){
        return embedded.getApiRecentSearchList();
    }

    @Override
    public String toString() {
        return  "_embedded : {" + embedded +
                '}';
    }

    private class Service {

        @SerializedName("tupleBackedMaps")
        private ArrayList<RecentSearch> recentSearchList = new ArrayList<>();

        public ArrayList<RecentSearch> getApiRecentSearchList() {
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
