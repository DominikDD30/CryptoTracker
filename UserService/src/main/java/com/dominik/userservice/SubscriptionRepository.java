package com.dominik.userservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Integer> {


    @Query("""
            FROM SubscriptionEntity s
             WHERE s.symbol= :crypto AND s.isActive AND
            (abs(:price - s.currentPrice) / s.currentPrice) * 100 > s.percentOffset
            """)
    List<SubscriptionEntity> findAllWhereOffSetOverLimit(String crypto, BigDecimal price);
}
