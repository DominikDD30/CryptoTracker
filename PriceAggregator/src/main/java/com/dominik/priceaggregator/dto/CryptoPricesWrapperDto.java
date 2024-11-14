package com.dominik.priceaggregator.dto;

import java.util.List;

public record CryptoPricesWrapperDto(List<CryptoPriceDto> priceDtos) {
}
