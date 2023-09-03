package com.example.productservice.port.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMessage {
    @JsonProperty
    @NotNull
    private String topic;

    @JsonProperty
    private long correlationID;

    @JsonProperty
    private String data; // nullable

    public CustomMessage(String topic, String data) {
        this.topic = topic;
        Random rand = new Random();
        this.correlationID = rand.nextLong();
        this.data = data;
    }
}
