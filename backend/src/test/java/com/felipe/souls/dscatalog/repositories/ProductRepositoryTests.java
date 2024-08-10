package com.felipe.souls.dscatalog.repositories;

import com.felipe.souls.dscatalog.entities.Product;
import com.felipe.souls.dscatalog.factories.Factory;
import com.felipe.souls.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        countTotalProduct = productRepository.count();
    }


    @Test
    public void findByIdShouldReturnObjectWhenIdExist() {
        Optional<Product> product = productRepository.findById(existingId);

        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals(existingId, product.get().getId());
    }

    @Test
    public void findByIdShouldReturnObjectNullWhenDoesNotIdExist() {
        Optional<Product> product = productRepository.findById(nonExistingId);
        Assertions.assertEquals(true, product.isEmpty());
    }


    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProduct + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExist() {
        productRepository.deleteById(existingId);

        Optional<Product> product = productRepository.findById(existingId);

        Assertions.assertFalse(product.isPresent());
    }

//    @Test
//    public void deleteShouldThrowEmptyResultResourceNotFoundExceptionWhenIdDoesNotExist() {
//        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//            productRepository.deleteById(100L);
//        });
//    }
}
