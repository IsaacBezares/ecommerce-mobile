package com.bessarez.ecommercemobile.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Cart {

    private Long id;
    private LocalDate updatedAt;
    private RegisteredUser registeredUser;
    private Set<CartItem> cartItems;

    public Cart() {
    }

    public Cart(Long id, LocalDate updatedAt, RegisteredUser registeredUser, Set<CartItem> cartItems) {
        this.id = id;
        this.updatedAt = updatedAt;
        this.registeredUser = registeredUser;
        this.cartItems = cartItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", registeredUser=" + registeredUser +
                ", cartItems=" + cartItems +
                '}';
    }
}
