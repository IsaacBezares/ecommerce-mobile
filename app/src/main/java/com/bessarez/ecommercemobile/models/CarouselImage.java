package com.bessarez.ecommercemobile.models;

import java.util.Objects;

public class CarouselImage {

    private Long id;
    private String imageUrl;
    private String linkUrl;

    public CarouselImage() {
    }

    public CarouselImage(Long id, String imageUrl, String linkUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarouselImage that = (CarouselImage) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CarouselImage{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}
