package com.dominik.alertservice.restClients;

import com.dominik.alertservice.dto.CryptoPriceDto;
import com.dominik.alertservice.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceRestClient {

    private final String baseUrl = "http://user-service.cryptoapp.svc.cluster.local:8080";

    private final RestClient restClient;

    public List<SubscriptionDto> getSubscriptionsThatExceedLimit(CryptoPriceDto crypto) {
        return  restClient.get()
                .uri(builder->builder
                        .scheme("http")
                        .host("user-service.cryptoapp.svc.cluster.local")
                        .port(8080)
                        .path("/subscriptions/"+crypto.symbol()+"/"+crypto.price())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }


}
