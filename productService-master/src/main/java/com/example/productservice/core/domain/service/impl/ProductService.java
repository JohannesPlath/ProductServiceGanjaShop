package com.example.productservice.core.domain.service.impl;

import com.example.productservice.core.domain.model.Product;
import com.example.productservice.core.domain.service.interfaces.IProductRepository;
import com.example.productservice.core.domain.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    @Autowired
    IProductRepository repository;

    @Override
    @Caching(
            evict = {@CacheEvict(value = "product_cache", allEntries = true)},
            put = {@CachePut(value = "one_product_cache", key = "#product.getId()")}
    )
    public Product createProduct(Product product) {
        return repository.save(product);
    }
    @Override
    @Cacheable(value = "one_product_cache", key = "#id")
    public Optional<Product> getProduct(UUID id) {
        log.info("I am in " + this.getClass().getSimpleName() + " -> not cached");
        return repository.findById(id);
    }

    @Override
    @Cacheable(value = "product_cache")
    public List<Product> getAllProducts() {
        log.info("I am in " + this.getClass().getSimpleName() + " -> not cached");
        return repository.findAll();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "product_cache", allEntries = true),
            @CacheEvict(value = "one_product_cache", key = "#id")}
    )
    public void deleteProduct(UUID id) {
        repository.deleteById(id);
    }
}
