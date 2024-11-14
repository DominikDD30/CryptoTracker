package com.dominik.priceaggregator;


import com.dominik.priceaggregator.dto.CryptoPriceDto;
import com.dominik.priceaggregator.dto.CryptoPricesWrapperDto;
import com.dominik.priceaggregator.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class KafkaCryptoPricesListener {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final CryptoService cryptoService;

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "crypto-prices",groupId = "groupId",containerFactory="listenerFactory")
    void listener(CryptoPricesWrapperDto data){
        System.out.println("Listener receive data: "+data);
        data.priceDtos().forEach(crypto->savePriceToRedis(crypto).subscribe());
        data.priceDtos().forEach(cryptoService::saveToPriceHistory);
    }

    private Mono<Void> savePriceToRedis(CryptoPriceDto crypto) {
        var valueOperations = reactiveRedisTemplate.opsForValue();
        return valueOperations.set(crypto.symbol(), crypto.price().toString(), Duration.ofSeconds(100)).then();
    }

}
