package com.example.productservice.core.domain.service.impl;

import com.example.productservice.core.domain.model.Category;
import com.example.productservice.core.domain.model.Currency;
import com.example.productservice.core.domain.model.Product;
import com.example.productservice.core.domain.service.interfaces.IProductRepository;
import com.example.productservice.core.domain.service.interfaces.IProductService;
import com.example.productservice.port.config.RedisConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@Import({ RedisConfig.class, ProductService.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
class ProductServiceTest {
    @MockBean
    private IProductRepository productRepositoryMock;


    @Autowired
    private IProductService productService;

    @Autowired
    private CacheManager cacheManager;


    @TestConfiguration
    static class EmbeddedRedisConfiguration {
        private final RedisServer redisServer;

        public EmbeddedRedisConfiguration() {
            this.redisServer = new RedisServer();
        }
        @PostConstruct
        public void startRedis() {
            this.redisServer.start();
        }
        @PreDestroy
        public void stopRedis() {
            this.redisServer.stop();
        }
    }

   @Test
    void givenRedisCaching_whenFindItemById_thenItemReturnedFromCache() {
        Product product = new Product();
        UUID id = UUID. randomUUID();
        product.setId(id);
        given(productRepositoryMock.findById(id)).willReturn(Optional.of(product));

        Product productRepo = productService.getProduct(id).get();
        Product productCache = productService.getProduct(id).get();

        assertThat(productRepo).isEqualTo(product);
        assertThat(productCache).isEqualTo(product);

        verify(productRepositoryMock, times(1)).findById(id);
    }

    @Test
    void testCreateProductResetsGetProductCache() {
        Product product = new Product();
        UUID id = UUID. randomUUID();
        product.setId(id);
        List<Product> productList = List.of(product);
        when(productRepositoryMock.findAll()).thenReturn(productList);

        List<Product> allProductsRepo = productService.getAllProducts();
        List<Product> allProductsCache = productService.getAllProducts();
        assertThat(allProductsRepo).isEqualTo(productList);
        assertThat(allProductsCache).isEqualTo(productList);

        UUID newId = UUID. randomUUID();
        Product newProduct = new Product(newId,"Chicken Boneless Burner", Currency.EURO, Category.DOPE, 2, 12.5, "high intensive dark smoked weed", "http://localhost:8081/pic/bobble");

        productService.createProduct(newProduct);

        List<Product> getAllProductsAfterRepoChange = productService.getAllProducts();

        verify(productRepositoryMock, times(2)).findAll();
    }

    @Test
    void testGetAllProductsUsesCache() {
        List<Product> productList = List.of(createProduct(0), createProduct(1), createProduct(2));
        when(productRepositoryMock.findAll()).thenReturn(productList);

        List<Product> allProductsRepo = productService.getAllProducts();
        List<Product> allProductsCache = productService.getAllProducts();

        assertThat(allProductsRepo).isEqualTo(productList);
        assertThat(allProductsCache).isEqualTo(productList);

        verify(productRepositoryMock, times(1)).findAll();
    }

    @Test
    void testCreateProductResetsGetAllProductsCache() {
        Product product = new Product();
        UUID id = UUID. randomUUID();
        product.setId(id);
        List<Product> productList = List.of(product);
        when(productRepositoryMock.findAll()).thenReturn(productList);

        List<Product> allProductsRepo = productService.getAllProducts();
        List<Product> allProductsCache = productService.getAllProducts();
        assertThat(allProductsRepo).isEqualTo(productList);
        assertThat(allProductsCache).isEqualTo(productList);

        UUID newId = UUID. randomUUID();
        Product newProduct = new Product(newId,"Chicken Boneless Burner", Currency.EURO, Category.DOPE, 2, 12.5, "high intensive dark smoked weed", "http://localhost:8081/pic/bobble");

        productService.createProduct(newProduct);

        List<Product> getAllProductsAfterRepoChange = productService.getAllProducts();
        List<Product> allProductsCache2 = productService.getAllProducts();

        verify(productRepositoryMock, times(2)).findAll();
    }


    private Product createProduct(int count) {
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
        return prefillProducts.get(count);
    }
}