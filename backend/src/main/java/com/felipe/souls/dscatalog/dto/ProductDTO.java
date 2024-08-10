package com.felipe.souls.dscatalog.dto;

import com.felipe.souls.dscatalog.entities.Category;
import com.felipe.souls.dscatalog.entities.Product;
import org.springframework.beans.BeanUtils;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Instant date;
    private Double price;
    private String imgUrl;

    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Instant date, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        date = product.getDate();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
    }

    public ProductDTO(Product product, Set<Category> categories) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        date = product.getDate();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

}
