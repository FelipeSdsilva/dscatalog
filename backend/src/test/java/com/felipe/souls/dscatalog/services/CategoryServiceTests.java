package com.felipe.souls.dscatalog.services;

import com.felipe.souls.dscatalog.dto.CategoryDTO;
import com.felipe.souls.dscatalog.entities.Category;
import com.felipe.souls.dscatalog.factories.Factory;
import com.felipe.souls.dscatalog.repositories.CategoryRepository;
import com.felipe.souls.dscatalog.services.exceptions.DatabaseException;
import com.felipe.souls.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Category category;
    private CategoryDTO categoryDTO;


    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 2L;
        boolean verify = true;
        category = Factory.createCategory();
        List<Category> list = List.of(category);
        categoryDTO = Factory.createCategoryDTO();

        Mockito.when(categoryRepository.findAll()).thenReturn(list);
        Mockito.when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.doThrow(ResourceNotFoundException.class).when(categoryRepository).getReferenceById(nonExistingId);
        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(category);
        Mockito.when(categoryRepository.existsById(existingId)).thenReturn(verify);
        Mockito.doThrow(DataIntegrityViolationException.class).when(categoryRepository).deleteById(dependentId);
        Mockito.when(categoryRepository.existsById(nonExistingId)).thenReturn(!verify);
        Mockito.when(categoryRepository.existsById(dependentId)).thenReturn(verify);
    }

    @Test
    public void findAllPagedShouldReturnPage() {

        List<CategoryDTO> result = categoryService.retrieverAllCategories();

        Assertions.assertNotNull(result);
        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    public void findByIdShouldReturnObjectDTOWhenIdExist() {
        CategoryDTO result = categoryService.findCategoryPerId(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.findCategoryPerId(nonExistingId));
    }

    @Test
    public void updateShouldReturnObjectNotEqualsWhenIdExist() {
        categoryDTO.setName("NameUpdate");
        CategoryDTO result = categoryService.updateCategoryPerId(existingId, categoryDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), categoryDTO.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategoryPerId(nonExistingId, categoryDTO));
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> categoryService.deleteCategoryPerId(dependentId));
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategoryPerId(nonExistingId));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> categoryService.deleteCategoryPerId(existingId));

        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(existingId);
    }
}
