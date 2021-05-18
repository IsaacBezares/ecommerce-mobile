package com.bessarez.ecommercemobile.models.apimodels;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class ApiRegisteredUser {
    @SerializedName("id")
    private Long id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("password")
    private String password;

    @SerializedName("shipAddress")
    private String shipAddress;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zipCode")
    private String zipCode;

    @SerializedName("country")
    private String country;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("registeredAt")
    private LocalDate registeredAt;

    public ApiRegisteredUser() {
    }

    public ApiRegisteredUser(Long id) {
        this.id = id;
    }

    public ApiRegisteredUser(Long id, String firstName, String lastName, String password, String shipAddress, String city, String state, String zipCode, String country, String phone, String email, LocalDate registeredAt) {
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
        this.registeredAt = registeredAt;
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

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }
}
