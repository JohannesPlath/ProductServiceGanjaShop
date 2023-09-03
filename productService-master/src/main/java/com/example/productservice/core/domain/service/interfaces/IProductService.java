package com.example.productservice.core.domain.service.interfaces;

import com.example.productservice.core.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public interface IProductService {

    Product createProduct(Product product);

    Optional<Product> getProduct(UUID id);

    List<Product> getAllProducts();

    void deleteProduct(UUID id);
}
