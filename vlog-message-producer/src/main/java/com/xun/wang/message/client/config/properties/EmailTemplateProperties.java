package com.xun.wang.message.client.config.properties;

import lombok.Data;

/**
 * @ClassName SimpleEmailAmqpProperties
 * @Description simpleEmail有关amqp配置
 * @Author xun.d.wang
 * @Date 2019/12/10 14:59
 * @Version 1.0
 **/
@Data
public class EmailTemplateProperties {

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
