package com.example.repository;

import static com.example.util.ProductConstants.PRODUCT;
import static com.example.util.ProductConstants.PRODUCT_EMPTY;
import static com.example.util.ProductConstants.PRODUCT_INVALID;
import static com.example.util.ProductConstants.PRODUCT_INEXISTENT;
import static com.example.util.ProductConstants.PRODUCT_NULL;
import static com.example.util.ProductConstants.PRODUCT_01_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;

import com.example.model.Product;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@AfterEach
	public void afterEach() {
		PRODUCT.setId(null);
	}

	@Test
	public void createValidProduct() {
		Product product = productRepository.save(PRODUCT);

		var sut = testEntityManager.find(Product.class, product.getId());

		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(product.getName());
		assertThat(sut.getModel()).isEqualTo(product.getModel());
		assertThat(sut.getCode()).isEqualTo(product.getCode());
		assertThat(sut.getPrice()).isEqualTo(product.getPrice());
	}

	@Test
	public void createInvalidProduct() {

		assertThatThrownBy(() -> productRepository.save(PRODUCT_NULL)).isInstanceOf(InvalidDataAccessApiUsageException.class);
		assertThatThrownBy(() -> productRepository.save(PRODUCT_INVALID)).isInstanceOf(ConstraintViolationException.class);
		assertThatThrownBy(() -> productRepository.save(PRODUCT_EMPTY)).isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	public void createDuplicatedProduct() {
		Product product = testEntityManager.persistFlushFind(PRODUCT);
		testEntityManager.detach(product);
		product.setId(null);

		assertThatThrownBy(() -> productRepository.save(product)).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@Sql(scripts = "/data/import_products.sql")
	public void readAllProduct() {

		Iterable<Product> list = productRepository.findAll();

		assertThat(list).isNotNull();
		assertThat(list).isNotEmpty();
		assertThat(list).hasSize(3);
	}

	@Test
	public void readOneProduct() {

		Product product = testEntityManager.persistFlushFind(PRODUCT);

		Optional<Product> sut = productRepository.findById(product.getId());

		assertThat(sut).isNotEmpty();
	}

	@Test
	public void updateValidProduct() {
		Product product = testEntityManager.persistFlushFind(PRODUCT);
		product.setName(PRODUCT_01_UPDATED.getName());

		Product productUpdated = productRepository.save(product);

		assertThat(productUpdated).isNotNull();
		assertThat(productUpdated.getId()).isEqualTo(PRODUCT_01_UPDATED.getId());
		assertThat(productUpdated.getName()).isEqualTo(PRODUCT_01_UPDATED.getName());

	}

	@Test
	public void deleteValidProduct() {

		Product product = testEntityManager.persistFlushFind(PRODUCT);

		assertThatCode(() -> productRepository.deleteById(product.getId())).doesNotThrowAnyException();
	}

	@Test
	public void deleteInvalidProduct() {

		assertThatThrownBy(() -> productRepository.deleteById(PRODUCT_INEXISTENT.getId())).isInstanceOf(EmptyResultDataAccessException.class);
	}

}
