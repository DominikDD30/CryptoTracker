package com.dominik.userservice;

import java.math.BigDecimal;

public record SubscriptionRequest(String symbol , BigDecimal currentPrice, BigDecimal percentOffSet, String email) {
}
