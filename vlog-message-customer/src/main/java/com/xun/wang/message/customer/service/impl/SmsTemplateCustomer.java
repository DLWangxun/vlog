package com.xun.wang.message.customer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.xun.wang.message.customer.config.properties.SmsProperties;
import com.xun.wang.message.customer.enums.MsgTypeEnums;
import com.xun.wang.vlog.email.api.IMessageApi;
import com.xun.wang.vlog.email.domain.SmsParam;
import com.xun.wang.vlog.email.domain.SmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SmsTemplateCustomer
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 17:49
 * @Version 1.0
 **/
@Slf4j
@Service
public class SmsTemplateCustomer {

    @Autowired
    private IMessageApi messageApi;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsProperties smsProperties;

    @Value("${message.customer.retry-times}")
    private Integer retryTimes;

    @RabbitListener(queues = "sms_template_queue")
    public void sendSimpleEmailToPerson(@Payload SmsParam smsParam, @Headers Map<String, Object> headers, Channel channel) {

        try {
            String smsText = replaceParamByValue(smsParam.getSmsTemplate());
            Map<String,Object> requestParam = new HashMap<String, Object>(){{
                put("uid",smsProperties.getUid());
                put("key",smsProperties.getKey());
                put("smsMob",smsParam.getSmsMob());
                put("smsText",smsText);
            }};
            URI uri = UriComponentsBuilder.fromUriString(smsProperties.getSendSmsUrl()).build()
                    .expand(requestParam).encode().toUri();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri,null,String.class);
            channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
        } catch (Exception e) {
            // 获取重试次数
            Integer currentRetryTimes = (Integer) headers.get("retryTimes");
            try {
                if (currentRetryTimes > retryTimes) {
                    channel.basicReject((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
                } else {
                    // 消息发送到队尾
                    messageApi.reSend(MsgTypeEnums.SMS.getMsgType(), currentRetryTimes, headers.get(AmqpHeaders.MESSAGE_ID).toString(), objectMapper.writeValueAsString(smsParam));
                    //消息確認,拉閘就gg
                    channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
                }
            } catch (IOException q) {
                log.info("消息发布失败 message = {}, e = {}", smsParam.toString(), q);
            }
        }
    }

    /**
     * 替换变量
     * @param smsTemplate
     * @return
     */
    public static String replaceParamByValue(SmsTemplate smsTemplate){
        String templateContext = smsTemplate.getTemplateContext();
        String signal = smsTemplate.getSign();
        Map<String,String> param = smsTemplate.getParams();
        if(StringUtils.isNotBlank(signal)){
            templateContext = String.format("[%s]%s",signal,templateContext);
        }
        String regex = "\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(templateContext);
        while(matcher.find()){
            String paramKey = matcher.group(1);
            String paramValue = param.get(paramKey);
            templateContext = matcher.replaceFirst(paramValue);
            matcher = pattern.matcher(templateContext);
        }
        return templateContext;
    }
}
