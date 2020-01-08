package com.xun.wang.message.client.service.impl;


import com.xun.wang.message.client.service.MsgService;
import com.xun.wang.vlog.email.domain.EmailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Service;


/**
 * @ClassName SendToPersonAmqpService
 * @Description 实现发送简单email
 * @Author xun.d.wang
 * @Date 2019/12/10 11:17
 * @Version 1.0
 **/
@Service("emailMsgService")
@Slf4j
public class EmailMsgService extends MsgService<EmailParam> {




    @Override
    public boolean send(EmailParam param, MessagePostProcessor messageProperties) {
        // 发送消息
        rabbitTemplate.convertAndSend(msgProperties.getEmailTemplate().getWorkExchangeName(), msgProperties.getEmailTemplate().getWorkRoutingKey(),param, messageProperties);
        return true;
    }



    @Override
    public boolean retrySend(EmailParam param,  MessagePostProcessor messageProperties) {
        // 发送消息
        rabbitTemplate.convertAndSend(msgProperties.getEmailTemplate().getRetryExhangeName(), msgProperties.getEmailTemplate().getRetryRoutingKey(), param, messageProperties);
        return true;
    }
}

