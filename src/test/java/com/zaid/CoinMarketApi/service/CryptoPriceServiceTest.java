package com.zaid.CoinMarketApi.service;

import com.zaid.CoinMarketApi.model.CryptoPrice;
import com.zaid.CoinMarketApi.repository.CryptoPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CryptoPriceServiceTest {

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    @InjectMocks
    private CryptoPriceService cryptoPriceService;

    private final String TEST_COIN = "BTC";
    private final CryptoPrice MOCK_PRICE = new CryptoPrice(
            1L, TEST_COIN,
            BigDecimal.valueOf(65000.00),
            LocalDateTime.now(),
            BigDecimal.valueOf(1000000));

    @Test
    void whenGetLatestPriceIsCalled_shouldReturnLatestPriceFromRepository() {
        when(cryptoPriceRepository.findTopByCoinOrderByTimestampDesc(TEST_COIN))
                .thenReturn(MOCK_PRICE);

        Optional<CryptoPrice> result = cryptoPriceService.getLatestPrice(TEST_COIN);

        assertTrue(result.isPresent(), "Coin Harus Ditemukan");
        assertEquals(TEST_COIN, result.get().getCoin(), "Coin Harus Cocok");

        verify(cryptoPriceRepository, times(1)).findTopByCoinOrderByTimestampDesc(TEST_COIN);
    }

    @Test
    void whenfetchAndSavePriceIsCalled_shouldReturnNewPrice() {
        when(cryptoPriceRepository.save(any(CryptoPrice.class)))
                .thenReturn(MOCK_PRICE);

        CryptoPrice saved = cryptoPriceService.fetchAndSavePrice(TEST_COIN);

        assertNotNull(saved, "Objek yang disimpan tidak boleh null");

        verify(cryptoPriceRepository, times(1)).save(any(CryptoPrice.class));
    }

}
