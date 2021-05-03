package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

public class ApiRecentSearch {
    @SerializedName("search")
    String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ApiRecentSearch{" +
                "name='" + name + '\'' +
                '}';
    }
}
