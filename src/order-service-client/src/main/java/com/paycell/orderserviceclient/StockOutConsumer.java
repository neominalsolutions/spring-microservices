package com.paycell.orderserviceclient;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class StockOutConsumer {

    @Bean
    public Consumer<String> stockOutCompleted() {
        return message -> {
            System.out.println("stockOutCompleted");
        };
    }

    @Bean
    public Consumer<String> stockOutFailed() {
        return message -> {
            System.out.println("stockOutFailed");
        };
    }

}
