package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.UserOrder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiUserOrders {
    @SerializedName("_embedded")
    private Service embedded;

    public Service getEmbedded() {
        return embedded;
    }

    public List<UserOrder> getEmbeddedServices() {
        return embedded.getUserOrderList();
    }

    private class Service {

        @SerializedName("userOrders")
        private ArrayList<UserOrder> userOrderList = new ArrayList<>();

        public List<UserOrder> getUserOrderList() {
            return userOrderList;
        }
    }
}
