package com.paycell.productserviceclient.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckStockResponse {

    private String message;
    private String status;
}
