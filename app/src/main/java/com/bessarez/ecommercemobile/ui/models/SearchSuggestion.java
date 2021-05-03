package com.bessarez.ecommercemobile.ui.models;

import java.util.Objects;

public class SearchSuggestion {

    private Long id;
    private String name;
    //1 = recent search, 2 = searchSuggestion
    private int type;

    public SearchSuggestion() {
    }

    public SearchSuggestion(Long id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchSuggestion that = (SearchSuggestion) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
