package com.bessarez.ecommercemobile.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RegisteredUser {

    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String shipAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;
    private String email;
    private Date date;
    private Set<UserViewedProduct> userViewedProducts = new HashSet<>();

    public RegisteredUser() {
    }

    public RegisteredUser(Long id, String firstName, String lastName, String password, String shipAddress, String city, String state, String zipCode, String country, String phone, String email, Date date, Set<UserViewedProduct> userViewedProducts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.shipAddress = shipAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.userViewedProducts = userViewedProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<UserViewedProduct> getUserViewedProducts() {
        return userViewedProducts;
    }

    public void setUserViewedProducts(Set<UserViewedProduct> userViewedProducts) {
        this.userViewedProducts = userViewedProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisteredUser)) return false;
        RegisteredUser registeredUser = (RegisteredUser) o;
        return id.equals(registeredUser.id) &&
                date.equals(registeredUser.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", userViewedProducts=" + userViewedProducts +
                '}';
    }
}
