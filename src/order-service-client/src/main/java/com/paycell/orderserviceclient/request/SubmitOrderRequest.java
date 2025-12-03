package com.paycell.orderserviceclient.request;

import lombok.Data;

@Data
public class SubmitOrderRequest {

    private String code;
    private Integer quantity;

}
