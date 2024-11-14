package com.dominik.priceaggregator.controller;

import com.dominik.priceaggregator.dto.CryptoPriceDto;
import com.dominik.priceaggregator.service.CryptoService;
import com.dominik.priceaggregator.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crypto")
public class CryptoController {

    private final PriceService priceService;
    private final CryptoService cryptoService;


    @GetMapping("/prices")
    public Mono<List<CryptoPriceDto>> getLatestCryptoPrices() {
        System.out.println("Received request for /crypto/prices");
        return priceService.getAllPrices();
    }

    @GetMapping("/prices/hourly")
    public List<CryptoPriceDto> getHourlyAverageCryptoPrices() {
        return cryptoService.getHourlyPrices();
    }

    @GetMapping("/prices/daily")
    public List<CryptoPriceDto> getDailyAverageCryptoPrices() {
        return cryptoService.getDailyPrices();
    }

    @PostMapping("/calculate/last-hour")
    public void calculateLastHour(){
        Map<String, BigDecimal> lastHourHistory = cryptoService.getLastHourHistory();
        lastHourHistory.entrySet().forEach(entry->cryptoService.saveLastHourAverage(entry.getKey(),entry.getValue()));
    }



}
