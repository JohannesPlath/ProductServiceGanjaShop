package com.example.productservice.port.model;

import com.example.productservice.core.domain.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ProductDTO {
    @JsonProperty
    @NotNull
    private final UUID id;

    @JsonProperty
    @NotNull
    private final String title;

    @JsonProperty
    private final int count;

    @JsonProperty
    private final double price;

    @JsonProperty
    @NotNull
    private final String description;

    @JsonProperty
    @NotNull
    private final String url;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.count = product.getCount();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.url = product.getUrl();
    }
}
