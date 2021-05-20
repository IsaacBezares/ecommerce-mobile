package com.bessarez.ecommercemobile.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.List;

public class UserOrder {

    private Long id;

    private LocalDate orderedAt;

    private List<OrderProduct> orderProducts;

    private RegisteredUser registeredUser;


    public UserOrder() {
    }

    public UserOrder(List<OrderProduct> orderProducts, RegisteredUser registeredUser) {
        this.orderProducts = orderProducts;
        this.registeredUser = registeredUser;
    }

    public UserOrder(Long id, LocalDate orderedAt, List<OrderProduct> orderProducts, RegisteredUser registeredUser) {
        this.id = id;
        this.orderedAt = orderedAt;
        this.orderProducts = orderProducts;
        this.registeredUser = registeredUser;
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

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
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
                ", orderedAt=" + orderedAt +
                ", orderProducts=" + orderProducts +
                ", registeredUser=" + registeredUser +
                '}';
    }
}
