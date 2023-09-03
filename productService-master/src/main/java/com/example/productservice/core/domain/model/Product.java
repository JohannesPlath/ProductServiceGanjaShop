package com.example.productservice.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @JsonProperty
    @NotNull
    private UUID id;

    @JsonProperty
    @NotNull
    private String title;

    @JsonProperty
    @NotNull
    private Currency currency;

    @JsonProperty
    @NotNull
    private Category category;

    @JsonProperty
    private int count;

    @JsonProperty
    private double price;

    @JsonProperty
    @NotNull
    private String description;

    @JsonProperty
    @NotNull
    private String url;

    public Product(String title, Currency currency, Category category, int count, double price, String description, String url) {
        this.title = title;
        this.currency = currency;
        this.category = category;
        this.count = count;
        this.price = price;
        this.description = description;
        this.url = url;
    }
}
