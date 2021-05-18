package com.bessarez.ecommercemobile.ui.models;

public class CardOrderItem extends ListOrderItem {

    private long id;

    private String imageUrl;

    private String name;

    public CardOrderItem() {
    }

    public CardOrderItem(long id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return TYPE_ORDER_ITEM;
    }

    @Override
    public String toString() {
        return "CardOrderItem{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
