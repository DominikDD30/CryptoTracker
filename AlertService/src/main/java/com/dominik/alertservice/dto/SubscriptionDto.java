package com.dominik.alertservice.dto;

import java.math.BigDecimal;

public record SubscriptionDto(String symbol , BigDecimal oldPrice, BigDecimal
        newPrice, BigDecimal percentOffSetLimit, String changedPercent, String notificationEmail) {
    public String toJSON() {
        return String.format("""
                {
                  "symbol": "%s",
                  "oldPrice": %s,
                  "newPrice": %s,
                  "percentOffSetLimit": %s,
                  "changedPercent": "%s",
                  "notificationEmail": "%s"
                }""",
                symbol,
                oldPrice.toPlainString(),
                newPrice.toPlainString(),
                percentOffSetLimit.toPlainString(),
                changedPercent,
                notificationEmail
        );
    }
}
