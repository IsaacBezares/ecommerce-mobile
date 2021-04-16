package com.bessarez.ecommercemobile.models;

import java.util.Date;
import java.util.Objects;

public class UserViewedProduct {
    private Long id;
    private RegisteredUser registeredUser;
    private Product product;
    private Date viewDate;

    public UserViewedProduct() {
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

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserViewedProduct that = (UserViewedProduct) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserViewedProduct{" +
                "id=" + id +
                ", registeredUser=" + registeredUser +
                ", product=" + product +
                ", viewDate=" + viewDate +
                '}';
    }
}
