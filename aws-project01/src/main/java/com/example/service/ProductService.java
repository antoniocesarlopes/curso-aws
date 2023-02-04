package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.exception.RequiredObjectIsNullException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Product;
import com.example.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}
	
	public Product create(Product product) {
		if(product == null) {
			throw new RequiredObjectIsNullException();
		}
			return productRepository.save(product);
	}
	
	public Product findById(Long id) {
		Optional<Product> optProduct = productRepository.findById(id);
		
		if(optProduct.isPresent()) {
			return optProduct.get();
		}else {
			throw new ResourceNotFoundException();
		}
	}
	
	public Iterable<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Product update(Long id, Product productToUpdate) {
		if(id == null || productToUpdate == null) {
			throw new RequiredObjectIsNullException();
		}
		
		if(productRepository.existsById(id)) {
			productToUpdate.setId(id);
			return productRepository.save(productToUpdate);
		}else {
			throw new ResourceNotFoundException();
		}
	}
	
	public Product delete(Long id) {
		var product = productRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException());
		productRepository.delete(product);
		return product;
	}

	public Product findByCode(String code) {
		Optional<Product> product = productRepository.findByCode(code);
		
		if(product.isPresent()) {
			return product.get();
		}else {
			throw new ResourceNotFoundException();
		}
	}
	
}
