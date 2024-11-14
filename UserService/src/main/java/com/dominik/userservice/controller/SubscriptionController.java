package com.dominik.userservice.controller;

import com.dominik.userservice.SubscriptionDto;
import com.dominik.userservice.SubscriptionRequest;
import com.dominik.userservice.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @PostMapping
    public void saveSubscription(@RequestBody SubscriptionRequest subscriptionDto){
        subscriptionService.saveSubscription(subscriptionDto);
    }

    @GetMapping("/{crypto}/{price}")
    public List<SubscriptionDto> getSubscriptionsThatExceedLimitForCrypto(@PathVariable String crypto,
                                                                          @PathVariable String price){
        return subscriptionService.getSubscriptionsThatExceedForCrypto(crypto,new BigDecimal(price));
    }
}
