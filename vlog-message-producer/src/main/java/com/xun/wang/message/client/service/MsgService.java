package com.xun.wang.message.client.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xun.wang.message.client.config.properties.MsgProperties;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;


/**
 * @ClassName IEmailService
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/16 15:51
 * @Version 1.0
 **/
public abstract class MsgService<T> {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MsgProperties msgProperties;

    @Autowired
    protected RabbitTemplate rabbitTemplate;


    public boolean sendProccess(String paramStr) throws IOException {
        // 设置消息唯一标识
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 设置消息头
        MessagePostProcessor messageProperties = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息持久化
                message.getMessageProperties().getHeaders().put("retryTimes", 1);
                message.getMessageProperties().setMessageId(correlationData.getId());
                return message;
            }
        };
        return send(getRequestBody(paramStr),messageProperties);
    }

    public boolean retrySendProccess(String paramStr, Integer currentRetryTimes, String messageId) throws IOException {
        // 设置消息唯一标识
        CorrelationData correlationData = new CorrelationData(messageId);
        // 设置消息头
        MessagePostProcessor messageProperties = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息持久化
                message.getMessageProperties().getHeaders().put("retryTimes", currentRetryTimes + 1);
                message.getMessageProperties().setMessageId(correlationData.getId());
                message.getMessageProperties().setExpiration(String.valueOf(currentRetryTimes* msgProperties.getRetryTimeSlot()));
                return message;
            }
        };
        return retrySend(getRequestBody(paramStr), messageProperties);
    }

    /**
     * 入参转实体
     *
     * @param paramStr
     * @return
     * @throws IOException
     */
    T getRequestBody(String paramStr) throws IOException {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper.readValue(paramStr, clazz);
    }

    /**
     * 发送
     *
     * @param param
     * @return
     */
    public abstract boolean send(T param, MessagePostProcessor messageProperties);


    /**
     * 重发
     *
     * @param param
     * @return
     */
    public abstract boolean retrySend(T param, MessagePostProcessor messageProperties);


}


