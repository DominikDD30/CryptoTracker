package com.dominik.priceaggregator.dto;

import java.math.BigDecimal;

public record CryptoPriceDto(String symbol, BigDecimal price) { }
