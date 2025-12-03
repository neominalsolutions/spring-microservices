package com.paycell.orderserviceclient.client;

import com.paycell.orderserviceclient.request.SubmitOrderRequest;
import com.paycell.orderserviceclient.response.SubmitOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// product-client -> application name
// spring.application.name=order-service-client

@Component
@FeignClient("product-service-client")
public interface ProductClient {


    @PostMapping("/api/v1/products/checkStock")
    ResponseEntity<SubmitOrderResponse> checkStock(@RequestBody SubmitOrderRequest request);

}
