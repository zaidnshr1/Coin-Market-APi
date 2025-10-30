package com.zaid.CoinMarketApi.service;

import com.zaid.CoinMarketApi.model.CryptoPrice;
import com.zaid.CoinMarketApi.repository.CryptoPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CryptoPriceService {

    private final CryptoPriceRepository coinPriceRepository;

    public CryptoPrice fetchAndSavePrice(String coin) {
        BigDecimal simulatedPrice = simulateExternalApiCall(coin);
        CryptoPrice newPrice = new CryptoPrice(
                null,
                coin.toUpperCase(),
                simulatedPrice,
                LocalDateTime.now(),
                BigDecimal.valueOf(1000000)
        );
        return coinPriceRepository.save(newPrice);
    }

    private BigDecimal simulateExternalApiCall(String coin) {
        switch (coin.toUpperCase()){
            case "BTC":
                return BigDecimal.valueOf(60000 + Math.random() * 10000).setScale(2, RoundingMode.HALF_UP);
            case "ETH":
                return BigDecimal.valueOf(3000 + Math.random() * 1000).setScale(2, RoundingMode.HALF_UP);
            default:
                return BigDecimal.ZERO;
        }
    }

    @Cacheable(value = "latestPrice", key = "#coin")
    public Optional<CryptoPrice> getLatestPrice(String coin) {
        System.out.println("Fetching from DB/API for coin: " + coin);
        CryptoPrice latestPrice = coinPriceRepository.findTopByCoinOrderByTimestampDesc(coin.toUpperCase());
        return Optional.ofNullable(latestPrice);
    }

}
