package com.dominik.alertservice;


import com.dominik.alertservice.dto.CryptoPriceDto;
import com.dominik.alertservice.dto.SubscriptionDto;
import com.dominik.alertservice.restClients.PriceServiceRestClient;
import com.dominik.alertservice.restClients.UserServiceRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final UserServiceRestClient userServiceRestClient;
    private final PriceServiceRestClient priceServiceRestClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Scheduled(fixedDelay = 30000)
    public void checkAlerts() {
        System.out.println("checking crypto prices");
        List<CryptoPriceDto> cryptos = priceServiceRestClient.getCurrentPrices();
        System.out.println(cryptos.get(0)+"total size "+cryptos.size());

        List<SubscriptionDto> alerts = cryptos.stream()
                .map(userServiceRestClient::getSubscriptionsThatExceedLimit)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .toList();


        System.out.println(alerts);

        alerts.forEach(alert ->
                {
                    try {
                        kafkaTemplate.send("alerts", objectMapper.writeValueAsString(alert));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

    }
}
