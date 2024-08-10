package com.felipe.souls.dscatalog.repositories;

import com.felipe.souls.dscatalog.entities.Category;
import com.felipe.souls.dscatalog.factories.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalCategory;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        countTotalCategory = categoryRepository.count();
    }
 

    @Test
    public void findByIdShouldReturnObjectWhenIdExist() {
        Optional<Category> category = categoryRepository.findById(existingId);

        Assertions.assertTrue(category.isPresent());
        Assertions.assertEquals(existingId, category.get().getId());
    }

    @Test
    public void findByIdShouldReturnObjectNullWhenDoesNotIdExist() {
        Optional<Category> category = categoryRepository.findById(nonExistingId);
        Assertions.assertEquals(true, category.isEmpty());
    }


    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Category category = Factory.createCategory();
        category = categoryRepository.save(category);

        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(countTotalCategory + 1, category.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExist() {
        categoryRepository.deleteById(existingId);

        Optional<Category> category = categoryRepository.findById(existingId);

        Assertions.assertFalse(category.isPresent());
    }

//    @Test
//    public void deleteShouldThrowEmptyResultResourceNotFoundExceptionWhenIdDoesNotExist() {
//        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//            categoryRepository.deleteById(100L);
//        });
//    }
}
