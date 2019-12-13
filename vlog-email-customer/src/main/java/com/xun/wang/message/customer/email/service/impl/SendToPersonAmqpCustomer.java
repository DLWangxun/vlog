package com.xun.wang.message.customer.email.service.impl;

import com.rabbitmq.client.Channel;
import com.xun.wang.vlog.email.api.ISimpleEmailService;
import com.xun.wang.vlog.email.domain.SimpleEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName SendToPersonAmqpCustomer
 * @Description rabbitTemplate 实现消费端
 * @Author xun.d.wang
 * @Date 2019/12/11 18:42
 * @Version 1.0
 **/
@Slf4j
@Service
public class SendToPersonAmqpCustomer {


    @Autowired
    private JavaMailSender simpleSender;

    @Autowired
    private ISimpleEmailService simpleEmailService;

    @Value("${email.customer.simple.retry-times}")
    private Integer retryTimes;


    @RabbitListener(queues = "simple_email_queue")
    public void sendSimpleEmailToPerson(@Payload SimpleEmail simpleEmail, @Headers Map<String, Object> headers, Channel channel) {
        if (!channel.isOpen()) {
            log.error("rabbit通道关闭");
            return;
        }
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(simpleEmail.getSender());
            simpleMailMessage.setTo(simpleEmail.getReciever());
            simpleMailMessage.setSubject(simpleEmail.getSubject());
            simpleMailMessage.setText(simpleEmail.getContent());
            simpleSender.send(simpleMailMessage);
            channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
        } catch (Exception e) {
            // 获取重试次数
            Integer currentRetryTimes = (Integer) headers.get("retryTimes");
            try {
                if (currentRetryTimes > retryTimes) {
                    channel.basicReject((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
                } else {
                    // 消息发送到队尾
                    simpleEmailService.retrySendSimpleEmailToPerson(simpleEmail, currentRetryTimes,  headers.get(AmqpHeaders.MESSAGE_ID).toString());
                    //消息確認,拉閘就gg
                    channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
                }
            } catch (IOException q) {
                log.info("消息发布失败 message = {}, e = {}", simpleEmail.toString(), q);
            }
        }
    }
}
