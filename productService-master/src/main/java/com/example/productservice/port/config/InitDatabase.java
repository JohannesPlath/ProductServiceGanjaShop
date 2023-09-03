package com.example.productservice.port.config;


import com.example.productservice.core.domain.model.Category;
import com.example.productservice.core.domain.model.Currency;
import com.example.productservice.core.domain.model.Product;
import com.example.productservice.core.domain.service.interfaces.IProductRepository;
import com.example.productservice.port.producer.ProductProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class InitDatabase {

    @Bean
    CommandLineRunner initDatabaseRunner(IProductRepository repository, ProductProducer productProducer) {
        return args -> {

            List<Product> prefillProducts = List.of(
            new Product("Chicken Boneless Burner", Currency.EURO, Category.DOPE, 2, 12.5, "high intensive dark smoked weed", "http://localhost:8081/pic/bobble"),
            new Product("Hawaiian", Currency.EURO, Category.DOPE, 3, 9.5, "Fancy visionary light dope", "http://localhost:8081/pic/cannabis-gb5c924ecf_1920"),
            new Product("Purple Haze", Currency.EURO, Category.DOPE, 2, 11.5, "Classic purple dope", "http://localhost:8081/pic/purple-hemp-flowers-medical-cannabis"),
            new Product("Lollipop", Currency.EURO, Category.PROCESSED, 1, 2.5, "Sweet lollipop with 1g thc", "http://localhost:8081/pic/justin-aikin-5Dyrcn5ocfk-unsplash.jpg"),
            new Product("Orange Bud", Currency.EURO, Category.DOPE, 3, 13.5, "The classic dope", "http://localhost:8081/pic/cambridge-jenkins-iv-KJbK6WqpGd4-unsplash.jpg"),
            new Product("Love and Peace Cup", Currency.EURO, Category.NON_DOPE, 2, 3.5, "A cup, a f****** cup", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIXS93Vb0IKHbrv4qGhLV4S0m637GXzaAXieRiAePatOhjl9G_1xjHjy0gCUiUFrUwEEo&usqp=CAU"),
            new Product("White Widow", Currency.EURO, Category.DOPE, 2, 10.73, "Classic fine turned Dope with a fres blend", "http://localhost:8081/pic/whiteWidow.jpg"),
            new Product("Roled One", Currency.EURO, Category.PROCESSED, 1, 6.25, "Blended sativa sticky", "http://localhost:8081/pic/rolled.jpg")
            );

            for (Product product : prefillProducts) {
                log.info("Init Database with: " + product);
                productProducer.productCreated(repository.save(product));
            }
        };
    }
}
