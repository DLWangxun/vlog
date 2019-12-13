package com.xun.wang.message.client.email.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName SimpleEmailAmqpProperties
 * @Description simpleEmail有关amqp配置
 * @Author xun.d.wang
 * @Date 2019/12/10 14:59
 * @Version 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix ="email.producer.simple.amqp")
public class SimpleEmailAmqpProperties {

    private String workExchangeName;

    private String workQueueName;

    private String workRoutingKey;

    private String retryExhangeName;

    private String retryQueueName;

    private String retryRoutingKey;

    private String failExchangeName;

    private String failQueueName;

    private String failRoutingKey;

    private Integer retryTimeSlot;

    private Integer defaultExpressTime;

}
