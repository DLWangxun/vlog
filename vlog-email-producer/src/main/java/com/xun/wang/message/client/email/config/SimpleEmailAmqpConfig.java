package com.xun.wang.message.client.email.config;

import com.xun.wang.message.client.email.config.properties.SimpleEmailAmqpProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SimpleEmailConfig
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/10 14:53
 * @Version 1.0
 **/
@Configuration
public class SimpleEmailAmqpConfig {

    @Autowired
    private SimpleEmailAmqpProperties simpleEmailAmqpProperties;

    /**
     * 声明工作交换机的名称
     * @return
     */
    @Bean
    DirectExchange workExchange() {
        return new DirectExchange(simpleEmailAmqpProperties.getWorkExchangeName());
    }

    /**
     * 声明工作队列
     * @return
     */
    @Bean
    Queue workQueue() {
        Map<String, Object> args = new HashMap<>(2);
        //声明重试交换器
        args.put("x-dead-letter-exchange",simpleEmailAmqpProperties.getFailExchangeName());
        //声明重试路由键
        args.put("x-dead-letter-routing-key",simpleEmailAmqpProperties.getFailRoutingKey());
        return new Queue(simpleEmailAmqpProperties.getWorkQueueName(), true, false, false,args);
    }

    @Bean
    Binding bindingWork() {
        return BindingBuilder.bind(workQueue()).to(workExchange()).with(simpleEmailAmqpProperties.getWorkRoutingKey());
    }

    /**
     * 声明重试交换机的名称
     * @return
     */
    @Bean
    DirectExchange retryExchange() {
        return new DirectExchange(simpleEmailAmqpProperties.getRetryExhangeName());
    }

    /**
     * 声明重试队列
     * @return
     */
    @Bean
    Queue retryQueue() {
        Map<String, Object> args = new HashMap<>(2);
        //声明重试交换器
        args.put("x-dead-letter-exchange",simpleEmailAmqpProperties.getWorkQueueName());
        //声明重试路由键
        args.put("x-dead-letter-routing-key",simpleEmailAmqpProperties.getWorkRoutingKey());
        //声明队列消息过期时间
        args.put("x-message-ttl", 1000);
        return new Queue(simpleEmailAmqpProperties.getRetryQueueName(), true, false, false,args);
    }

    @Bean
    Binding bindingRetry() {
        return BindingBuilder.bind(retryQueue()).to(retryExchange()).with(simpleEmailAmqpProperties.getRetryRoutingKey());
    }

    /**
     * 声明失败交换机的名称
     * @return
     */
    @Bean
    DirectExchange failExchange() {
        return new DirectExchange(simpleEmailAmqpProperties.getFailExchangeName());
    }

    /**
     * 声明失败队列
     * @return
     */
    @Bean
    Queue failQueue() {
        return new Queue(simpleEmailAmqpProperties.getFailQueueName(), true);
    }


    @Bean
    Binding bindingFail() {
        return BindingBuilder.bind(failQueue()).to(failExchange()).with(simpleEmailAmqpProperties.getFailRoutingKey());
    }
}
