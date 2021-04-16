package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseApiRegisteredUsers {
    @SerializedName("_embedded")
    private Service embedded;

    public List<RegisteredUser> getEmbeddedServices() {
        return embedded.getRegisteredUsers();
    }

    private class Service {

        @SerializedName("registeredUsers")
        private ArrayList<RegisteredUser> registeredUserList = new ArrayList<>();

        public List<RegisteredUser> getRegisteredUsers() {
            return registeredUserList;
        }
    }
}
