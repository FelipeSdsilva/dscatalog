package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long contTotalProduct;
	private Page<ProductDTO> page;
	private PageRequest pageRequest;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		contTotalProduct = 25L;
		pageRequest = PageRequest.of(0, 10, Sort.by("name"));
	
	}

	
	@Test
	public void deleteShouldDeleteResourceWhenIdExist() {

		service.delete(existingId);

		Assertions.assertEquals(contTotalProduct - 1, repository.count());
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void findAllPagesShouldReturnPageWhenPageSize10() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		page =service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(contTotalProduct, page.getTotalElements());
	}

	@Test
	public void findAllPagesShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		pageRequest = PageRequest.of(59, 10);
		page =service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(page.isEmpty());
	}
	
	@Test
	public void findAllPagesShouldReturnSortedPageWhenSortByName() {

		page = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", page.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", page.getContent().get(2).getName());
	}

	
}
