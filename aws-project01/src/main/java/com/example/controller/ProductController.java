package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enums.ProductEventType;
import com.example.model.Product;
import com.example.service.ProductPublisher;
import com.example.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private ProductService productService;
	private ProductPublisher productPublisher;

	public ProductController(ProductService productService, ProductPublisher productPublisher) {
		this.productService = productService;
		this.productPublisher = productPublisher;
	}
	
	@PostMapping
	public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
		Product productCreated = productService.create(product);

		productPublisher.publishProductEvent(productCreated, ProductEventType.PRODUCT_CREATED, "APP");
		
		return new ResponseEntity<Product>(productCreated, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(@PathVariable long id) {
		Product product = productService.findById(id);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@GetMapping
	public Iterable<Product> findAll() {
		return productService.findAll();
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable("id") long id) {
		Product productUpdated = productService.update(id, product);
		
		productPublisher.publishProductEvent(productUpdated, ProductEventType.PRODUCT_UPDATE, "APP");

		return new ResponseEntity<Product>(productUpdated, HttpStatus.OK);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
		Product productDeleted = productService.delete(id);
		
		productPublisher.publishProductEvent(productDeleted, ProductEventType.PRODUCT_DELETED, "APP");
		
		return new ResponseEntity<Product>(HttpStatus.OK);
	}

	@GetMapping(path = "/bycode")
	public ResponseEntity<Product> findByCode(@RequestParam String code) {
		Product optProduct = productService.findByCode(code);
		return new ResponseEntity<Product>(optProduct, HttpStatus.OK);
	}
}