package com.xun.wang.message.customer.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "retry")
public class RetryProperties {

    private BackOffProperties backOff;

    private PolicyProperties policy;


}
