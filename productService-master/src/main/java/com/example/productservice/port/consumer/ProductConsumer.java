package com.example.productservice.port.consumer;

import com.example.productservice.core.domain.model.Product;
import com.example.productservice.core.domain.service.interfaces.IProductService;
import com.example.productservice.port.model.CustomMessage;
import com.example.productservice.port.producer.ProductProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductConsumer {
    private static final String ANSWER_PREFIX = "answer.";
    private static final String GET_PRODUCT_BY_ID = "getProductByID";
    private static final String GET_ALL_PRODUCTS = "getAllProducts";
    private static final String CREATE_PRODUCT = "createProduct";
    private static final String DELETE_PRODUCT = "deleteProduct";
    private static final String ERROR = "error";
    private static final String NOT_FOUND = "notFound";
    @Autowired
    private IProductService productService;

    @Autowired
    private ProductProducer productProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = {"origin_product_queue"})
    public CustomMessage handleRequest(CustomMessage message) {
        log.info("Handling message: " + message);
        return handleMessage(message);
    }

    private CustomMessage handleMessage(CustomMessage requestMessage)  {
        long responseCorrelationId = requestMessage.getCorrelationID();
        String responseTopic = ANSWER_PREFIX.concat(requestMessage.getTopic());
        String responseData = null;
        String requestTopic = requestMessage.getTopic();
        try {
            switch (requestTopic) {
                case GET_PRODUCT_BY_ID -> {
                    UUID productId = UUID.fromString(requestMessage.getData());
                    log.info("GET product by id: " + productId);
                    Optional<Product> productOptional = productService.getProduct(productId);
                    if (productOptional.isPresent()) {
                        responseData = objectMapper.writeValueAsString(productOptional.get());
                    } else {
                        responseTopic = ANSWER_PREFIX.concat(NOT_FOUND);
                        responseData = productId.toString();
                    }
                }
                case GET_ALL_PRODUCTS -> {
                    log.info("GET all products");
                    List<Product> allProducts = productService.getAllProducts();
                    responseData = objectMapper.writeValueAsString(allProducts);
                }
                case CREATE_PRODUCT -> {
                    Product newProduct = objectMapper.readValue(requestMessage.getData(), Product.class);
                    log.info("CREATE new product: " + newProduct);
                    Product createdProduct = productService.createProduct(newProduct);
                    responseData = objectMapper.writeValueAsString(createdProduct);
                    productProducer.productCreated(createdProduct);
                }
                case DELETE_PRODUCT -> {
                    UUID productId = UUID.fromString(requestMessage.getData());
                    log.info("DELETE product by id: " + productId);
                    try {
                        productService.deleteProduct(productId);
                        productProducer.productDeleted(productId);
                    } catch (Exception e) {
                        responseTopic = ANSWER_PREFIX.concat(NOT_FOUND);
                        responseData = productId.toString();
                    }
                }
                default -> {
                    log.warn("unknown message topic: " + requestMessage.getTopic());
                    responseTopic = ANSWER_PREFIX.concat(ERROR);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            responseTopic = ANSWER_PREFIX.concat(ERROR);
        }
        return new CustomMessage(responseTopic, responseCorrelationId, responseData);
    }
}
