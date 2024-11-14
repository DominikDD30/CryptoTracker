package com.dominik.userservice.service;

import com.dominik.userservice.SubscriptionDto;
import com.dominik.userservice.SubscriptionRequest;
import com.dominik.userservice.SubscriptionEntity;
import com.dominik.userservice.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    public void saveSubscription(SubscriptionRequest subscriptionDto) {
        SubscriptionEntity entity = SubscriptionEntity.builder()
                .symbol(subscriptionDto.symbol())
                .currentPrice(subscriptionDto.currentPrice())
                .percentOffset(subscriptionDto.percentOffSet())
                .isActive(true)
                .email(subscriptionDto.email())
                .build();
        subscriptionRepository.save(entity);
    }


    public List<SubscriptionDto> getSubscriptionsThatExceedForCrypto(String crypto, BigDecimal price) {
        List<SubscriptionEntity> subscriptionEntityList = subscriptionRepository.findAllWhereOffSetOverLimit(crypto, price);
        List<SubscriptionDto> subscriptionDtos = subscriptionEntityList.stream()
                .map(entity -> SubscriptionDto.builder()
                        .symbol(entity.getSymbol())
                        .oldPrice(entity.getCurrentPrice())
                        .newPrice(price)
                        .percentOffSetLimit(entity.getPercentOffset())
                        .changedPercent(
                                (price.subtract(entity.getCurrentPrice()))
                                        .divide(entity.getCurrentPrice(), 5, RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(100))
                                        .stripTrailingZeros()
                                        .toPlainString() + "%")
                        .notificationEmail(entity.getEmail())
                        .build())
                .toList();


        subscriptionRepository.saveAll(subscriptionEntityList.stream().peek(subscription->subscription.setIsActive(false)).toList());
        return subscriptionDtos;
    }
}
