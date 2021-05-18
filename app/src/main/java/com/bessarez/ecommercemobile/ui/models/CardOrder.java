package com.bessarez.ecommercemobile.ui.models;

import java.time.LocalDate;

public class CardOrder extends ListOrderItem {

    private long id;

    private LocalDate orderedAt;

    private double totalAmount;

    public CardOrder() {
    }

    public CardOrder(long id, LocalDate orderedAt, double totalAmount) {
        this.id = id;
        this.orderedAt = orderedAt;
        this.totalAmount = totalAmount;
    }

    public long getId() {
        return id;
    }

    public LocalDate getOrderedAt() {
        return orderedAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public int getType() {
        return TYPE_ORDER;
    }

    @Override
    public String toString() {
        return "CardOrder{" +
                "id=" + id +
                ", orderedAt=" + orderedAt +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
