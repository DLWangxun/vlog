package com.xun.wang.message.customer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.xun.wang.vlog.email.api.IMessageApi;
import com.xun.wang.vlog.email.domain.EmailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName TemplateEmailCustomer
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/17 17:11
 * @Version 1.0
 **/
@Slf4j
@Service
public class EmailTemplateCustomer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IMessageApi messageApi;

    @Value("${message.customer.retry-times}")
    private Integer retryTimes;

    @Autowired
    private ObjectMapper objectMapper;

    //template模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    private final String EMAILTYPE = "email";


    @RabbitListener(queues = "email_template_queue")
    public void sendSimpleEmailToPerson(@Payload EmailParam emailParam, @Headers Map<String, Object> headers, Channel channel) {
        if (!channel.isOpen()) {
            log.error("rabbit通道关闭");
            return;
        }
        try {
            //创建message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(emailParam.getSender());
            messageHelper.setTo(emailParam.getReciever());
            messageHelper.setSubject(emailParam.getSubject());
            Context context = new Context();
            context.setVariable("name", emailParam.getName());
            context.setVariable("content", emailParam.getContent());
            context.setVariable("sender", emailParam.getSender());
            Arrays.stream(emailParam.getTemplate().getSources()).forEach(source -> {
                context.setVariable(source.getName(),source.getSourceUrl());
            });
            //获取thymeleaf的html模板
            String emailContent = templateEngine.process(emailParam.getTemplate().getTemplateUrl(), context); //指定模板路径
            messageHelper.setText(emailContent, true);
            mailSender.send(message);
            channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
        } catch (Exception e) {
            // 获取重试次数
            Integer currentRetryTimes = (Integer) headers.get("retryTimes");
            try {
                if (currentRetryTimes > retryTimes) {
                    channel.basicReject((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
                } else {
                    // 消息发送到队尾
                    messageApi.reSend(EMAILTYPE, currentRetryTimes, headers.get(AmqpHeaders.MESSAGE_ID).toString(), objectMapper.writeValueAsString(emailParam));
                    //消息確認,拉閘就gg
                    channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
                }
            } catch (IOException q) {
                log.info("消息发布失败 message = {}, e = {}", emailParam.toString(), q);
            }
        }
    }
}
