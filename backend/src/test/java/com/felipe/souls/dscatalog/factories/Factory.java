package com.felipe.souls.dscatalog.factories;

import com.felipe.souls.dscatalog.dto.CategoryDTO;
import com.felipe.souls.dscatalog.dto.ProductDTO;
import com.felipe.souls.dscatalog.entities.Category;
import com.felipe.souls.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(
                1L,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.com/img.png",
                Instant.parse("2024-08-09T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }

    public static Category createCategory() {
        return new Category(null, "New Category");
    }

    public static CategoryDTO createCategoryDTO() {
        Category category = createCategory();
        return new CategoryDTO(category);
    }
}
