package com.zaid.CoinMarketApi.controller;

import com.zaid.CoinMarketApi.model.CryptoPrice;
import com.zaid.CoinMarketApi.service.CryptoPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prices")
public class PriceController {

    private final CryptoPriceService cryptoPriceService;

    @PostMapping("/{coin}/fetch")
    public ResponseEntity<CryptoPrice> fetchPrice(@PathVariable String coin) {
        CryptoPrice savedPrice = cryptoPriceService.fetchAndSavePrice(coin);
        return ResponseEntity.status(201).body(savedPrice);
    }

    @GetMapping("/{coin}")
    public ResponseEntity getLatestPrice(@PathVariable String coin) {
        return cryptoPriceService.getLatestPrice(coin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{coin}/history")
    public ResponseEntity<List<CryptoPrice>> getPriceHistory(@PathVariable String coin) {
        List<CryptoPrice> history = cryptoPriceService.getPriceHistory(coin);

        if (history.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

}
