package com.xun.wang.message.client.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xun.wang.message.client.email.config.properties.SimpleEmailAmqpProperties;
import com.xun.wang.vlog.email.api.ISimpleEmailService;
import com.xun.wang.vlog.email.domain.SimpleEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * @ClassName SendToPersonAmqpService
 * @Description 实现发送简单email给个人
 * @Author xun.d.wang
 * @Date 2019/12/10 11:17
 * @Version 1.0
 **/
@Service
@Slf4j
public class SendToPersonService implements ISimpleEmailService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpleEmailAmqpProperties simpleEmailAmqpProperties;

    @Autowired
    private ObjectMapper objectMapper;


    private final Integer FIRSTSENDNUM = 1;

    @Override
    public boolean sendSimpleEmailToPerson(SimpleEmail simpleEmail) {
        // 设置消息唯一标识
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 设置消息头
        MessagePostProcessor messageProperties = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息持久化
                message.getMessageProperties().getHeaders().put("retryTimes", FIRSTSENDNUM);
                message.getMessageProperties().setMessageId(correlationData.getId());
                return message;
            }
        };
        // 发送消息
        rabbitTemplate.convertAndSend(simpleEmailAmqpProperties.getWorkExchangeName(), simpleEmailAmqpProperties.getWorkRoutingKey(), simpleEmail, messageProperties, correlationData);
        return true;
    }

    @Override
    public boolean retrySendSimpleEmailToPerson(SimpleEmail simpleEmail,Integer currentRetryTimes,String messageId) {
        // 设置消息唯一标识
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 设置消息头
        MessagePostProcessor messageProperties = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息持久化
                message.getMessageProperties().getHeaders().put("retryTimes", currentRetryTimes+1);
                message.getMessageProperties().setMessageId(correlationData.getId());
                message.getMessageProperties().setExpiration(String.valueOf( currentRetryTimes * simpleEmailAmqpProperties.getRetryTimeSlot()));
                return message;
            }
        };
        // 发送消息
        rabbitTemplate.convertAndSend(simpleEmailAmqpProperties.getRetryExhangeName(), simpleEmailAmqpProperties.getRetryRoutingKey(), simpleEmail, messageProperties, correlationData);
        return true;
    }

}
