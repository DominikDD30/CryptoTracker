package com.dominik.emailservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class Alert {
    private String symbol;
    private double oldPrice;
    private double newPrice;
    private double percentOffSetLimit;
    private String changedPercent;
    private String notificationEmail;




}
