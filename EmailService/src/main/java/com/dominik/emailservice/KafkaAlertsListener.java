package com.dominik.emailservice;


import com.dominik.emailservice.dto.Alert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaAlertsListener {

    private final MailService emailService;
    private final ObjectMapper objectMapper;

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "alerts", groupId = "groupId", containerFactory = "listenerFactory")
    void listener(String data) throws JsonProcessingException, MessagingException {
        System.out.println("Listener receive data: " + data);
        String unescapedData = data.substring(1, data.length() - 1);
        String unescapedJson = unescapedData.replace("\\\"", "\"");

        Alert alert = objectMapper.readValue(unescapedJson, Alert.class);
        System.out.println(alert);

               emailService.sendEmail(
                       alert.getNotificationEmail(),
                       String.format("Crypto Tracker new notification for crypto %s", alert.getSymbol()),
                       """
                               Dear Subscriber

                               We would like to inform you about a recent price change for the cryptocurrency: %s
                               The price has changed from %s to %s, which represents a change of %s%%.


                               Your set threshold percentage for alerts is: %s%%.

                               Best regards,
                               Crypto Tracker Team
                                               """.formatted(alert.getSymbol(),alert.getOldPrice(),alert.getNewPrice(),alert.getChangedPercent(),alert.getPercentOffSetLimit())
               );
    }


}
