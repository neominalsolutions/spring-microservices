package com.paycell.productserviceclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceClientApplication.class, args);
	}

}
