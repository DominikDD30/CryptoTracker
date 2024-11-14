package com.dominik.alertservice.dto;

import java.util.List;
import java.util.stream.Collectors;

public record AlertsWrapper(List<SubscriptionDto> alerts) {


    public String toJSON(){
        String alertsJson = alerts.stream()
                .map(SubscriptionDto::toJSON)
                .collect(Collectors.joining(","));
        return """
                {
                "alerts":[
                          %s
                          ]
                }""".formatted(alertsJson);
    }
}
