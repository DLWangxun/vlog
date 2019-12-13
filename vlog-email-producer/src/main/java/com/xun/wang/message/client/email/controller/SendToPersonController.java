package com.xun.wang.message.client.email.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xun.wang.vlog.email.api.ISimpleEmailService;
import com.xun.wang.vlog.email.domain.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName sendToPerson
 * @Description 消息发送给个人
 * @Author xun.d.wang
 * @Date 2019/12/3 11:22
 * @Version 1.0
 **/

@RestController
@RequestMapping("/simple/email")
public class SendToPersonController {

    @Autowired
    private ISimpleEmailService simpleEmailService;
    @Value("${my.test}")
    private String value;


    /**
     * 异步发送邮箱给个人
     * @param simpleEmail
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/send/person")
    public boolean sendToPerson(@RequestBody SimpleEmail simpleEmail) throws JsonProcessingException {
        // 发送消息
        return  simpleEmailService.sendSimpleEmailToPerson(simpleEmail);
    }

    /**
     * 重试发送简单email给个人
     * @param simpleEmail
     * @return
     */
    @PostMapping("/resend/person")
    public boolean retrySendSimpleEmailToPerson(@RequestBody SimpleEmail simpleEmail, @RequestParam Integer currentRetryTimes, String messageId){
        // 重试消息
        return  simpleEmailService.retrySendSimpleEmailToPerson(simpleEmail,currentRetryTimes,messageId);
    }
}
