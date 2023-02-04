package com.example.model.dto;

import com.example.enums.ProductEventType;

public class ProductEnvelope {
	
	private ProductEventType eventType;
	private String data;

	public ProductEventType getEventType() {
		return eventType;
	}

	public void setEventType(ProductEventType eventType) {
		this.eventType = eventType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
