package com.zaid.CoinMarketApi.repository;

import com.zaid.CoinMarketApi.model.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {
    CryptoPrice findTopByCoinOrderByTimestampDesc(String coin);
    List<CryptoPrice> findAllByCoinOrderByTimestampDesc(String coin);
}
