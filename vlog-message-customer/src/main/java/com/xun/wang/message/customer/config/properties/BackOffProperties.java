package com.xun.wang.message.customer.config.properties;

import lombok.Data;

@Data
public class BackOffProperties {

    private  Long interval;

    private  double multiplier;

    private  Long maxInterval;
}
