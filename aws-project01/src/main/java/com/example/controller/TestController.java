package com.example.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
	
	private Logger LOG = Logger.getLogger(TestController.class.getName());
	
	@GetMapping("dog/{name}")
	public ResponseEntity<String> dogTest(@PathVariable String name) {
		LOG.info("TestController ::: Name: " + name);
		return ResponseEntity.ok("Name: " + name);
	}
	
	@GetMapping("car/{name}")
	public ResponseEntity<String> carTest(@PathVariable String name) {
		LOG.info("TestController ::: Car Name: " + name);
		return ResponseEntity.ok("Car Name: " + name);
	}


}
