package com.dominik.priceaggregator.service;

import com.dominik.priceaggregator.entity.HistoryEntity;
import com.dominik.priceaggregator.repository.HistoryRepository;
import com.dominik.priceaggregator.dto.CryptoPriceDto;
import com.dominik.priceaggregator.entity.HourlyCryptoHistoryEntity;
import com.dominik.priceaggregator.repository.HourlyCryptoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final HistoryRepository cryptoRepository;
    private final HourlyCryptoHistoryRepository hourlyCryptoHistoryRepository;

    public void saveToPriceHistory(CryptoPriceDto crypto) {
        cryptoRepository.save(new HistoryEntity(
                0,
                crypto.symbol(),
                crypto.price(),
                LocalDateTime.now()));
    }

    public List<CryptoPriceDto> getHourlyPrices() {
        LocalDateTime thisHour = LocalDateTime.of(LocalDate.now(), LocalTime.now().withMinute(0).withSecond(0).withNano(0));
        List<HourlyCryptoHistoryEntity> pricesEntities = hourlyCryptoHistoryRepository.findAllByDate(thisHour).orElseGet(() ->
                hourlyCryptoHistoryRepository.findAllByDate(thisHour.minusHours(1)).orElse(List.of())
        );
        if(pricesEntities.isEmpty()){
            return List.of();
        }else{
         return   pricesEntities.stream()
                 .map(pricesEntity->new CryptoPriceDto(pricesEntity.getSymbol(),pricesEntity.getAverage())).toList();
        }
    }

    public Map<String, BigDecimal> getLastHourHistory() {
        LocalDateTime now = LocalDateTime.now();
        List<CryptoPriceDto> lastHourCryptos = cryptoRepository.findAllByDateBetween(now.minusHours(1),now);
        Map<String, BigDecimal> cryptosWithAveragePrices = lastHourCryptos.stream()
                .collect(Collectors.groupingBy(
                        CryptoPriceDto::symbol, // Group by symbol
                        Collectors.collectingAndThen(
                                Collectors.toList(), // Collect the items into a list
                                list -> {
                                    BigDecimal sum = list.stream()
                                            .map(CryptoPriceDto::price)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    long count = list.size(); // Get the count of elements for each symbol
                                    return sum.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_UP); // Calculate average
                                }
                        )
                ));

        return cryptosWithAveragePrices;
    }

    public void saveLastHourAverage(String symbol, BigDecimal averagePrice) {
        LocalDateTime thisHour = LocalDateTime.of(LocalDate.now(), LocalTime.now().withMinute(0).withSecond(0).withNano(0));
        hourlyCryptoHistoryRepository.save(new HourlyCryptoHistoryEntity(0,thisHour,symbol,averagePrice));
    }

    public List<CryptoPriceDto> getDailyPrices() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfTheDay = now.withHour(0);

        List<HourlyCryptoHistoryEntity> all = hourlyCryptoHistoryRepository
                .findAllByDateBetween(startOfTheDay, now)
                .orElseGet(List::of);

        Map<String, BigDecimal> dailyAverages = all.stream()
                .collect(Collectors.groupingBy(
                        HourlyCryptoHistoryEntity::getSymbol,
                        Collectors.collectingAndThen(
                                Collectors.mapping(HourlyCryptoHistoryEntity::getAverage, Collectors.toList()),
                                list -> list.stream()
                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                                        .divide(BigDecimal.valueOf(list.size()), BigDecimal.ROUND_HALF_UP)
                        )
                ));

        return dailyAverages.entrySet().stream()
                .map(entry -> new CryptoPriceDto(entry.getKey(), entry.getValue()))
                .toList();
    }

}
