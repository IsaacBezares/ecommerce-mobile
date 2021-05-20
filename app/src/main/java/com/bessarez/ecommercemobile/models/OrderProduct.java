package com.bessarez.ecommercemobile.models;

import java.util.Objects;

public class OrderProduct {

    private Long id;

    private int quantity;

    private UserOrder userOrder;

    private Product product;

    public OrderProduct() {
    }

    public OrderProduct(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public OrderProduct(Long id, int quantity, UserOrder userOrder, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.userOrder = userOrder;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(UserOrder userOrder) {
        this.userOrder = userOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", order=" + userOrder +
                ", product=" + product +
                '}';
    }
}