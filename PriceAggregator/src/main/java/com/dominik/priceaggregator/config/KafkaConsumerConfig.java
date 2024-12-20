package com.dominik.priceaggregator.config;

import com.dominik.priceaggregator.dto.CryptoPricesWrapperDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    public Map<String,Object> consumerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, CryptoPricesWrapperDto> consumerFactory() {
        JsonDeserializer<CryptoPricesWrapperDto> deserializer = new JsonDeserializer<>(CryptoPricesWrapperDto.class);
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(consumerConfig(),new StringDeserializer(), deserializer);
    }



    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CryptoPricesWrapperDto>> listenerFactory(
            ConsumerFactory<String, CryptoPricesWrapperDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CryptoPricesWrapperDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
