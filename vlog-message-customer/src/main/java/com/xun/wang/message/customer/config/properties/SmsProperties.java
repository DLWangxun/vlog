package com.xun.wang.message.customer.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SmsProperties
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 17:53
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = "message.sms")
public class SmsProperties {
    private String uid;
    private String key;
    private String sendSmsUrl;
}
