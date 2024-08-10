package com.felipe.souls.dscatalog.services;

import com.felipe.souls.dscatalog.dto.ProductDTO;
import com.felipe.souls.dscatalog.entities.Product;
import com.felipe.souls.dscatalog.factories.Factory;
import com.felipe.souls.dscatalog.repositories.ProductRepository;
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
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Product product;
    private ProductDTO productDTO;


    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 2L;
        boolean verify = true;
        product = Factory.createProduct();
        PageImpl<Product> page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDTO();

        Mockito.when(productRepository.findAllProductsWithCategories(ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).getReferenceById(nonExistingId);
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(productRepository.existsById(existingId)).thenReturn(verify);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(!verify);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(verify);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.allProductsPaginated(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findAllProductsWithCategories(pageable);
    }

    @Test
    public void findByIdShouldReturnObjectDTOWhenIdExist() {
        ProductDTO result = productService.findProductPerId(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findProductPerId(nonExistingId));
    }

    @Test
    public void updateShouldReturnObjectNotEqualsWhenIdExist() {
        productDTO.setName("NameUpdate");
        ProductDTO result = productService.updateProductPerId(existingId, productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), productDTO.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.updateProductPerId(nonExistingId, productDTO));
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> productService.deleteProductPerId(dependentId));
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.deleteProductPerId(nonExistingId));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> productService.deleteProductPerId(existingId));

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }
}
