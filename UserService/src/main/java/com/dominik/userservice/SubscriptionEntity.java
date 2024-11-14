package com.dominik.userservice;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String symbol;

    @Column(name = "percent_offset")
    private BigDecimal percentOffset;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    private String email;

    @Override
    public String toString() {
        return "SubscriptionEntity{" +
                "symbol='" + symbol + '\'' +
                ", percentOffset=" + percentOffset +
                ", currentPrice=" + currentPrice +
                ", notificationEmail='" + email + '\'' +
                '}';
    }
}
