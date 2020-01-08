package com.xun.wang.message.client.service.impl;

import com.xun.wang.message.client.service.MsgService;
import com.xun.wang.vlog.email.domain.SmsParam;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

/**
 * @ClassName SmsMsgService
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 16:36
 * @Version 1.0
 **/
@Service("smsMsgService")
public class SmsMsgService extends MsgService<SmsParam> {

    @Override
    public boolean send(SmsParam param, MessagePostProcessor messageProperties) {
        // 发送消息
        rabbitTemplate.convertAndSend(msgProperties.getSmsTemplate().getWorkExchangeName(), msgProperties.getSmsTemplate().getWorkRoutingKey(),param, messageProperties);
        return true;
    }



    @Override
    public boolean retrySend(SmsParam param,  MessagePostProcessor messageProperties) {
        // 发送消息
        rabbitTemplate.convertAndSend(msgProperties.getSmsTemplate().getRetryExhangeName(), msgProperties.getSmsTemplate().getRetryRoutingKey(), param, messageProperties);
        return true;
    }
}
