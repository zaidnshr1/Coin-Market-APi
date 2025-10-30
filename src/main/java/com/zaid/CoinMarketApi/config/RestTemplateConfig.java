package com.zaid.CoinMarketApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new org.springframework.http.client.SimpleClientHttpRequestFactory());
        ((org.springframework.http.client.SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(10000);
        ((org.springframework.http.client.SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(10000);
        return restTemplate;
    }

}
