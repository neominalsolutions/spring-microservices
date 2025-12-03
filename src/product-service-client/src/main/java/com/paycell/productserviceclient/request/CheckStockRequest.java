package com.paycell.productserviceclient.request;

import lombok.Data;

@Data
public class CheckStockRequest {

    private String code;
    private Integer quantity;

}
