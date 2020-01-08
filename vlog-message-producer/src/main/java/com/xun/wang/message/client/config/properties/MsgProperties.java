package com.xun.wang.message.client.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName MsgEmailProperties
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 14:54
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = "message.producer")
public class MsgProperties {

    private Integer retryTimeSlot;

    private EmailTemplateProperties emailTemplate;

    private SmsTemplateProperties smsTemplate;
}
