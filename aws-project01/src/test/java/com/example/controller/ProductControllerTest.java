package com.example.controller;

import static com.example.util.ProductConstants.PRODUCT_01;
import static com.example.util.ProductConstants.PRODUCT_LIST;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.service.ProductPublisher;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;
	
	@MockBean 
	ProductPublisher productPublisher;

	@Test
	public void readAllProduct() throws Exception {
		
		when(productService.findAll()).thenReturn(PRODUCT_LIST);
		
		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0]").value(PRODUCT_01));
	}
	
}
