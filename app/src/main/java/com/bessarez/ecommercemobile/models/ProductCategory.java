package com.bessarez.ecommercemobile.models;

import java.util.Objects;

public class ProductCategory {

    private Long id;
    private String category;
//    private Set<Product> products = new HashSet<>();

    public ProductCategory() {
    }

    public ProductCategory(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return id.equals(that.id) &&
                category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category);
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "id=" + id +
                ", category='" + category + '\'' +
                '}';
    }
}
