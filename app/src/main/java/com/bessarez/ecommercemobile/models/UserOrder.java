package com.bessarez.ecommercemobile.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserOrder {

    private Long id;

    private LocalDate orderedAt;

    private Set<OrderProduct> orderProducts = new HashSet<>();


    public UserOrder() {
    }

    public UserOrder(Long id, LocalDate orderedAt, Set<OrderProduct> orderProducts) {
        this.id = id;
        this.orderedAt = orderedAt;
        this.orderProducts = orderProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDate date) {
        this.orderedAt = date;
    }

    public Set<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOrder userOrder = (UserOrder) o;
        return id.equals(userOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", date=" + orderedAt +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
