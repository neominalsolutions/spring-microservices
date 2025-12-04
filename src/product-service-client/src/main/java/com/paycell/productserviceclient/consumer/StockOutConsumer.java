package com.paycell.productserviceclient.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paycell.productserviceclient.request.CheckStockRequest;
import com.paycell.productserviceclient.request.StockOutCompleted;
import com.paycell.productserviceclient.request.StockOutFailed;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class StockOutConsumer {
    private final ObjectMapper _objectMapper;
    private final StreamBridge _streamBridge;


    public StockOutConsumer(ObjectMapper objectMapper, StreamBridge streamBridge){
        _objectMapper = objectMapper;
        _streamBridge = streamBridge;
    }

    // stockOut method ismi ile stream.cloud.function.definationName eşleşmeli

    @Bean
    public Consumer<String> stockOut(){
        return message -> {


            try {
                CheckStockRequest payload =  _objectMapper.readValue(message, CheckStockRequest.class);

                // Consumer Hataya düşünce nasıl davranır ?
                if(payload.getCode().startsWith("PX")) {
                    var eventMessage = new StockOutFailed();
                    eventMessage.setCode(payload.getCode());
                    eventMessage.setReason("Sipariş kodu PX ile başlayamaz");

                    var eventPayload = _objectMapper.writeValueAsString(eventMessage);

                    _streamBridge.send("stockOutFailed-out-0",eventPayload);
                    // throw new RuntimeException("Sipariş kodu PX ile başlayamaz");
                } else {
                    var eventMessage = new StockOutCompleted();
                    eventMessage.setCode(payload.getCode());

                    var eventPayload = _objectMapper.writeValueAsString(eventMessage);

                    _streamBridge.send("stockOutCompleted-out-0",eventPayload);

                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            System.out.println("read Message" + message);
        };
    }


}
