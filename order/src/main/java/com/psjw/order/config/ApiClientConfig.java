package com.psjw.order.config;

import com.psjw.order.infrastructure.point.PointApiClient;
import com.psjw.order.infrastructure.product.ProductApiClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.web.client.RestClient;

@Configuration
public class ApiClientConfig {


    @Bean
    public ProductApiClient productApiClient() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(2));

        return new ProductApiClient(
                RestClient.builder()
                        .requestFactory(factory)
                        .baseUrl("http://localhost:8082")
                        .build()
        );
    }

    @Bean
    public PointApiClient pointApiClient() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(2));

        return new PointApiClient(
                RestClient.builder()
                        .requestFactory(factory)
                        .baseUrl("http://localhost:8081")
                        .build()
        );
    }

}
