package com.paycell.orderserviceclient.service;



import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SampleService {

    private final CircuitBreaker circuitBreaker;

    public SampleService(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreaker = circuitBreakerFactory.create("sampleService");
    }

    public String callExternalApi() {

        return circuitBreaker.run(
                this::doCall,
                throwable -> fallback(throwable)
        );
    }

    private String doCall() {
        int random = new Random().nextInt(10);

        if (random < 5) {
            throw new RuntimeException("External API error!");
        }

        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Success Response!";
    }

    private String fallback(Throwable t) {
        return "Fallback: " + t.getMessage();
    }
}
