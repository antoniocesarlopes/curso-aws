package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.example.enums.ProductEventType;
import com.example.model.Product;
import com.example.model.dto.ProductEnvelope;
import com.example.model.dto.ProductEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductPublisher {
	
    private static final Logger LOG = LoggerFactory.getLogger(ProductPublisher.class);
    private AmazonSNS snsClient;
    private Topic productEventsTopic;
    private ObjectMapper objectMapper;

    public ProductPublisher(AmazonSNS snsClient,
                            @Qualifier("productEventsTopic") Topic productEventsTopic,
                            ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
        this.objectMapper = objectMapper;
    }

    public void publishProductEvent(Product product, ProductEventType eventType, String username) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductId(product.getId());
        productEvent.setCode(product.getCode());
        productEvent.setUsername(username);

        ProductEnvelope envelope = new ProductEnvelope();
        envelope.setEventType(eventType);

        try {
            envelope.setData(objectMapper.writeValueAsString(productEvent));

            PublishResult publishResult =  snsClient.publish(
                    productEventsTopic.getTopicArn(),
                    objectMapper.writeValueAsString(envelope));
            
            LOG.info("Product event sent - Event: {} - ProductId: {} - MessageId: {}",
                    envelope.getEventType(),
                    productEvent.getProductId(),
                    publishResult.getMessageId());

        } catch (JsonProcessingException e) {
            LOG.error("Failed to create product event message");
        }
    }
}
