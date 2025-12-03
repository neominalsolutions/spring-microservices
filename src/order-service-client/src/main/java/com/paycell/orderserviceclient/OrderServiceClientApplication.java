package com.paycell.orderserviceclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// Clientları scan edip bulması için client tanımı yapılan projelerde feign client aktif edilir.
@EnableFeignClients
public class OrderServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceClientApplication.class, args);
	}

}
