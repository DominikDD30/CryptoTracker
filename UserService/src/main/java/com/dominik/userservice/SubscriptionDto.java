package com.dominik.userservice;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SubscriptionDto(String symbol , BigDecimal oldPrice, BigDecimal
        newPrice, BigDecimal percentOffSetLimit, String changedPercent, String notificationEmail) {
}
