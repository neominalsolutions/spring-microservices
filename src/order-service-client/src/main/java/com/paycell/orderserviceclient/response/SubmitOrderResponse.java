package com.paycell.orderserviceclient.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitOrderResponse {

    private String message;
    private String status;

}
