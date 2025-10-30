package com.zaid.CoinMarketApi.service;

import com.zaid.CoinMarketApi.model.CryptoPrice;
import com.zaid.CoinMarketApi.repository.CryptoPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CryptoPriceService {

    private final CryptoPriceRepository coinPriceRepository;
    private final RestTemplate restTemplate;

    public CryptoPrice fetchAndSavePrice(String coin) {
        BigDecimal realPrice = fetchPriceFromExternalApi(coin);
        CryptoPrice newPrice = new CryptoPrice(
                null,
                coin.toUpperCase(),
                realPrice,
                LocalDateTime.now(),
                BigDecimal.valueOf(1000000)
        );
        return coinPriceRepository.save(newPrice);
    }

    private BigDecimal fetchPriceFromExternalApi(String coin) {
        String externalCoinId = mapCoinToGeckoId(coin);

        if (externalCoinId.isEmpty()) {
            return BigDecimal.ZERO;
        }

        String url = String.format(
                "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd",
                externalCoinId
        );

        try {
            Map response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey(externalCoinId)) {
                Map priceData = (Map) response.get(externalCoinId);
                Number priceNumber = (Number) priceData.get("usd");
                Double price = priceNumber.doubleValue();
                System.out.println("Fetched REAL price from CoinGecko for " + coin + ": $" + price);
                return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to fetch price from CoinGecko for " + coin + ". " + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    private String mapCoinToGeckoId(String coin) {
        return switch (coin.toUpperCase()) {
            case "BTC" -> "bitcoin";
            case "ETH" -> "ethereum";
            default -> "";
        };
    }

    @Cacheable(value = "latestPrice", key = "#coin")
    public Optional<CryptoPrice> getLatestPrice(String coin) {
        System.out.println("Fetching from DB/API for coin: " + coin);
        CryptoPrice latestPrice = coinPriceRepository.findTopByCoinOrderByTimestampDesc(coin.toUpperCase());
        return Optional.ofNullable(latestPrice);
    }

}
