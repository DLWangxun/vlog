package com.xun.wang.message.client.config.properties;

import lombok.Data;

/**
 * @ClassName TemplateSmsProperties
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 14:58
 * @Version 1.0
 **/
@Data
public class SmsTemplateProperties {

    private String workExchangeName;

    private String workQueueName;

    private String workRoutingKey;

    private String retryExhangeName;

    private String retryQueueName;

    private String retryRoutingKey;

    private String failExchangeName;

    private String failQueueName;

    private String failRoutingKey;
}
