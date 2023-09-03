package com.example.productservice.port.producer;

import com.example.productservice.core.domain.model.Product;
import com.example.productservice.port.model.CustomMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductProducer {
    private static final String CREATE_PRODUCT = "createProduct";
    private static final String DELETE_PRODUCT = "deleteProduct";
    @Value("product_exchange")
    private String exchange;

    @Value("product_routing_key")
    private String routingKey;

    @Autowired
    private ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    public ProductProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void productCreated(Product createdProduct) throws JsonProcessingException {
        CustomMessage message = new CustomMessage(CREATE_PRODUCT, objectMapper.writeValueAsString(createdProduct));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void productDeleted(UUID productId) {
        CustomMessage message = new CustomMessage(DELETE_PRODUCT, productId.toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
