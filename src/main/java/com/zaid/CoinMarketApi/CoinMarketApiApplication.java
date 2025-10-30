package com.zaid.CoinMarketApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CoinMarketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinMarketApiApplication.class, args);
	}

}
