package com.paycell.productserviceclient.controller;

import com.paycell.productserviceclient.request.CheckStockRequest;
import com.paycell.productserviceclient.response.CheckStockResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {

    @PostMapping("checkStock")
    public ResponseEntity<CheckStockResponse> checkStock(@RequestBody CheckStockRequest request) throws Exception {

        // simule edelim
       Long maxStockLimit = Math.round(Math.random() * 100);
       log.info("maxLimit: " + maxStockLimit);

       if(request.getQuantity() > maxStockLimit){
           log.info("Canceled");
           throw  new Exception("Hatalı işlem");
//           return ResponseEntity.ok(new CheckStockResponse("Ürün sipariş edilemez. Tek seferde sipraiş limiti aşıldı","Canceled"));

       } else {
           log.info("Ok");
           return ResponseEntity.ok(new CheckStockResponse("Ürün Opsiyonlanabilir","OK"));
       }
    }

}
