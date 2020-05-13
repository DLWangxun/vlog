package com.xun.wang.message.customer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.xun.wang.vlog.email.api.IMessageApi;
import com.xun.wang.vlog.email.domain.EmailParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateRequeueAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    @Value("${message.email.sender}")
    private String sender;

    @Autowired
    private ObjectMapper objectMapper;

    //template模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RetryTemplate retryTemplate;

    private final String EMAILTYPE = "email";


    @RabbitListener(queues = "email_template_queue")
    public void sendSimpleEmailToPerson(@Payload EmailParam emailParam, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        if (!channel.isOpen()) {
            log.error("rabbit通道关闭");
            return;
        }
        //创建message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(sender);
        messageHelper.setTo(emailParam.getReciever());
        messageHelper.setSubject(emailParam.getSubject());
        Context context = new Context();
        //模版填空
        Class<?> targetClass = emailParam.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields ){
            if( Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String filedName = field.getName();
            context.setVariable(filedName,getFieldValue(emailParam,filedName));
        }
        //获取thymeleaf的html模板
        String emailContent = templateEngine.process(emailParam.getTemplate().getTemplateUrl(), context); //指定模板路径
        messageHelper.setText(emailContent, true);
        mailSender.send(message);
    }

    private Object getFieldValue(Object target,String filedName) throws Exception {
        Class<?> targetClass = target.getClass();
        String getMethod = "get".concat(StringUtils.capitalize(filedName));
        Method method = targetClass.getMethod(getMethod);
        return  method.invoke(target,null);
    }

}
