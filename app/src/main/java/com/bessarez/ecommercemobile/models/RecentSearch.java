package com.bessarez.ecommercemobile.models;

import java.util.Objects;

public class RecentSearch {

    private Long id;
    private String search;
    private RegisteredUser registeredUser;

    public RecentSearch() {
    }

    public RecentSearch(Long id, String search, RegisteredUser registeredUser) {
        this.id = id;
        this.search = search;
        this.registeredUser = registeredUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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
        RecentSearch that = (RecentSearch) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RecentSearch{" +
                "id=" + id +
                ", search='" + search + '\'' +
                ", registeredUser=" + registeredUser +
                '}';
    }
}
