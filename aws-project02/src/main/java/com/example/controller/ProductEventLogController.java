package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.dto.ProductEventLogDto;
import com.example.service.ProductEventLogService;

@RestController
@RequestMapping("/api")
public class ProductEventLogController {

    private ProductEventLogService productEventLogService;

    public ProductEventLogController(ProductEventLogService productEventLogService) {
        this.productEventLogService = productEventLogService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<ProductEventLogDto>> getAllEvents() { 
        return ResponseEntity.ok(productEventLogService.getAllEvents());
    }

    @GetMapping("/events/{code}")
    public ResponseEntity<List<ProductEventLogDto>> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(productEventLogService.findByCode(code));
    }

    @GetMapping("/events/{code}/{event}")
    public ResponseEntity<List<ProductEventLogDto>> findByCodeAndEventType(@PathVariable String code,
                                                           @PathVariable String event) {
        return ResponseEntity.ok(productEventLogService.findByCodeAndEventType(code, event));
    }
}

















