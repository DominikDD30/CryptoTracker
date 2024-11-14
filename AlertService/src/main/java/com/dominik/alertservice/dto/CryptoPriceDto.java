package com.dominik.alertservice.dto;

import java.math.BigDecimal;

public record CryptoPriceDto(String symbol, BigDecimal price) { }
