package com.vinsguru.geocrawler.config;

import com.vinsguru.geocrawler.client.GeoDataClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public GeoDataClient geoDataClient(RestClient.Builder builder, @Value("${geo-data-service.url}") String baseUrl) {
        var httpClient = HttpClient.newBuilder().executor(Executors.newVirtualThreadPerTaskExecutor()).build();
        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        var restClient = builder.baseUrl(baseUrl)
                                .requestFactory(requestFactory)
                                .build();
        return new GeoDataClient(restClient);
    }

}
