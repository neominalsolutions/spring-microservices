package com.paycell.productserviceclient.request;

import lombok.Data;

@Data
public class StockOutFailed {
    private String code;
    private String reason;
}


