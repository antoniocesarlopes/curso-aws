package com.example.service;

import static com.example.util.ProductConstants.PRODUCT_INVALID;
import static com.example.util.ProductConstants.PRODUCT;
import static com.example.util.ProductConstants.PRODUCT_01;
import static com.example.util.ProductConstants.PRODUCT_01_UPDATED;
import static com.example.util.ProductConstants.PRODUCT_INEXISTENT;
import static com.example.util.ProductConstants.PRODUCT_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.exception.RequiredObjectIsNullException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Product;
import com.example.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@InjectMocks
	ProductService productService;
	
	@Mock
	ProductRepository productRepository;
	
	@Test
	public void createValidProduct() {
		
		when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);
		
		Product sut = productService.create(PRODUCT);
		
		assertThat(sut).isNotNull();
		assertThat(sut.getId()).isEqualTo(PRODUCT.getId());
		assertThat(sut.getModel()).isEqualTo(PRODUCT.getModel());
		assertThat(sut).isEqualTo(PRODUCT);
	}
	
	@Test
	public void createNullProduct() {
		
		assertThatThrownBy(() -> productService.create(null)).isInstanceOfAny(RequiredObjectIsNullException.class);
	}
	
	@Test
	public void createInvalidProduct() {
		when(productRepository.save(PRODUCT_INVALID)).thenThrow(DataIntegrityViolationException.class);
		
		assertThatThrownBy(() -> productService.create(PRODUCT_INVALID)).isInstanceOf(DataIntegrityViolationException.class);
	}
	
	@Test
	public void readAllProduct() {
		
		when(productRepository.findAll()).thenReturn(PRODUCT_LIST);
		
		Iterable<Product> sut = productService.findAll();
		
		assertThat(sut).isNotNull();
		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(3);
		assertThat(sut.iterator().next()).isEqualTo(PRODUCT_LIST.get(0));
	}
	
	@Test
	public void readOneProduct() {
		
		when(productRepository.findById(PRODUCT_01.getId())).thenReturn(Optional.of(PRODUCT_01));
		
		var sut = Optional.of(productService.findById(PRODUCT_01.getId()));
		
		assertThat(sut.isPresent());
		assertThat(sut.get()).isNotNull();
		assertThat(sut.get().getId()).isEqualTo(PRODUCT_01.getId());
		assertThat(sut.get().getModel()).isEqualTo(PRODUCT_01.getModel());
	}
	
	@Test
	public void updateValidProduct() {
		
		when(productRepository.existsById(PRODUCT_01.getId())).thenReturn(true);
		when(productRepository.save(PRODUCT_01_UPDATED)).thenReturn(PRODUCT_01_UPDATED);
		
		var sut = productService.update(PRODUCT_01.getId(), PRODUCT_01_UPDATED);
		
		assertThat(sut).isNotNull();
		assertThat(sut.getId()).isEqualTo(PRODUCT_01.getId());
		assertThat(sut.getModel()).isNotEqualTo(PRODUCT_01.getModel());
		assertThat(sut).isEqualTo(PRODUCT_01_UPDATED);
	}
	
	@Test
	public void deleteValidProduct() {
		
		when(productRepository.findById(PRODUCT_01.getId())).thenReturn(Optional.of(PRODUCT_01));
		
		assertThatCode(() -> productService.delete(PRODUCT_01.getId())).doesNotThrowAnyException();
	}
	
	@Test
	public void deleteInvalidProduct() {
		
		when(productRepository.findById(PRODUCT_INEXISTENT.getId())).thenThrow(ResourceNotFoundException.class);
		
		assertThatThrownBy(() -> productService.delete(PRODUCT_INEXISTENT.getId())).isInstanceOf(ResourceNotFoundException.class);
	}
	
	
}
