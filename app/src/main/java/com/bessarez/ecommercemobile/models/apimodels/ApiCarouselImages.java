package com.bessarez.ecommercemobile.models.apimodels;

import com.bessarez.ecommercemobile.models.CarouselImage;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiCarouselImages {
    @SerializedName("_embedded")
    private Service embedded;

    public List<CarouselImage> getEmbeddedServices() {
        return embedded.getCarouselImages();
    }

    private class Service {

        @SerializedName("carouselImages")
        private ArrayList<CarouselImage> carouselImageList = new ArrayList<>();

        public List<CarouselImage> getCarouselImages() {
            return carouselImageList;
        }
    }
}
