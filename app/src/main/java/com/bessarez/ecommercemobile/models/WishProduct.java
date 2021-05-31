package com.bessarez.ecommercemobile.models;

import java.util.Objects;

public class WishProduct {

    private Long id;
    private RegisteredUser registeredUser;
    private Product product;

    public WishProduct() {
    }

    public WishProduct(Long id) {
        this.id = id;
    }

    public WishProduct(RegisteredUser registeredUser, Product product) {
        this.registeredUser = registeredUser;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
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
        WishProduct that = (WishProduct) o;
        return registeredUser.equals(that.registeredUser) &&
                product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registeredUser, product);
    }

    @Override
    public String toString() {
        return "WishProduct{" +
                "id=" + id +
                ", registeredUser=" + registeredUser +
                ", product=" + product +
                '}';
    }
}
