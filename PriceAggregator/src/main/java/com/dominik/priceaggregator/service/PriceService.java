package com.dominik.priceaggregator.service;

import com.dominik.priceaggregator.dto.CryptoPriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<List<CryptoPriceDto>> getAllPrices() {
        List<String> cryptoNames = List.of(
                "BTC", "ETH", "UNI", "LTC", "ADA", "DOGE", "OP", "DOT", "FIL", "SOL",
                "TRX", "USDC", "USDT", "ETC", "ATOM", "STX", "BCH", "KCS", "GRT", "LEO",
                "BNB", "TON", "ICP", "POL", "AAVE", "OM", "LINK", "VET", "XLM", "XMR",
                "XRP", "AVAX", "DAI"
        );


        Flux<CryptoPriceDto> cryptoPricesFlux = Flux.fromIterable(cryptoNames)
                .concatMap(crypto ->
                        reactiveRedisTemplate.opsForValue().get(crypto+"USDT")
                                .map(price -> new CryptoPriceDto(crypto, new BigDecimal(price)))
                );

        return cryptoPricesFlux.collectList();
    }
}