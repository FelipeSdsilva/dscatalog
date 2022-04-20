package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	

	private Long existingId;
	private Long nonExistingId;
	private Long contTotalProduct;
	private ProductDTO productDto;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		contTotalProduct = 25L;
		productDto = Factory.createProductDto();
	}

	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
		ResultActions result = 
				mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(contTotalProduct));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
		
	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExist() throws Exception{
		String bodyJson = objectMapper.writeValueAsString(productDto);
	
		String expectedName = productDto.getName();
		String expectedDescripiton = productDto.getDescription();

		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(bodyJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescripiton));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExixt() throws Exception {
		String bodyJson = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = 
				mockMvc.perform(get("/products/{id}", nonExistingId)
						.content(bodyJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
	
	
}
