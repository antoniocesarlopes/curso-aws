package com.example.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.model.dto.ProductEventLogDto;
import com.example.repository.ProductEventLogRepository;

@Service
public class ProductEventLogService {

	private static final Logger log = LoggerFactory.getLogger(ProductEventLogService.class);

	private ProductEventLogRepository productEventLogRepository;

	public ProductEventLogService(ProductEventLogRepository productEventLogRepository) {
		this.productEventLogRepository = productEventLogRepository;
	}

	public List<ProductEventLogDto> getAllEvents() {
		return StreamSupport.stream(productEventLogRepository.findAll().spliterator(), false)
				.map(ProductEventLogDto::new).collect(Collectors.toList());
	}

	public List<ProductEventLogDto> findByCode(String code) {
		return productEventLogRepository.findAllByPk(code).stream().map(ProductEventLogDto::new)
				.collect(Collectors.toList());
	}

	public List<ProductEventLogDto> findByCodeAndEventType(String code, String event) {
		return productEventLogRepository.findAllByPkAndSkStartsWith(code, event).stream().map(ProductEventLogDto::new)
				.collect(Collectors.toList());
	}
}
