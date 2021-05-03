package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

public class ApiSuggestedProduct {
    @SerializedName("name")
    String name;
    @SerializedName("id")
    Long id;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
