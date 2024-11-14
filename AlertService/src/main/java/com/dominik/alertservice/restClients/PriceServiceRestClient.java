package com.dominik.alertservice.restClients;

import com.dominik.alertservice.dto.CryptoPriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PriceServiceRestClient {

    private final String baseUrl = "http://price-aggregator-service.cryptoapp.svc.cluster.local:8081/crypto";

    private final RestClient restClient;

    public List<CryptoPriceDto> getCurrentPrices() {
        return  restClient.get()
                .uri(baseUrl + "/prices")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
