package com.zaid.CoinMarketApi.controller;

import com.zaid.CoinMarketApi.model.CryptoPrice;
import com.zaid.CoinMarketApi.service.CryptoPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
public class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CryptoPriceService cryptoPriceService;

    private final CryptoPrice MOCK_PRICE = new CryptoPrice(
            1L,
            "ETH",
            BigDecimal.valueOf(3500.00),
            LocalDateTime.now(),
            BigDecimal.valueOf(500000)
    );

    @Test
    void getLatestPrice_shoudReturnPrice_whenFound() throws Exception{
        when(cryptoPriceService.getLatestPrice(anyString()))
                .thenReturn(Optional.of(MOCK_PRICE));

        mockMvc.perform(get("/api/v1/prices/{coin}", "ETH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coin").value("ETH"))
                .andExpect(jsonPath("$.price").value(3500.00));
    }

    @Test
    void getLatestPrice_shouldReturnNotFound_whenNotPresent() throws Exception{
        when(cryptoPriceService.getLatestPrice(anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/prices/{coin}", "DOGE"))
                .andExpect(status().isNotFound());
    }

}
