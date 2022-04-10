package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L,"Phone","Good Phone",800.0,"https://img.com/img.png",Instant.parse("2022-04-10T10:30:00Z"));
		product.getCategories().add(new Category(2L,"Electronic"));
		return product;
	}
	
	public static ProductDTO createProductDto() {
		Product product = createProduct();
		return new ProductDTO(product,product.getCategories());
	}
	
}
