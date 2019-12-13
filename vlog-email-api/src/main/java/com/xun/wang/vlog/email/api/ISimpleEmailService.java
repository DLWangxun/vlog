package com.xun.wang.vlog.email.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xun.wang.vlog.email.domain.SimpleEmail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${email.server.name}",
             path = "/simple/email")
public interface ISimpleEmailService {


    /**
     * 发送简单email给个人
     * @param simpleEmail
     */
    @PostMapping("/send/person")
    boolean  sendSimpleEmailToPerson(@RequestBody SimpleEmail simpleEmail) throws JsonProcessingException;

    /**
     * 重试发送简单email给个人
     * @param simpleEmail
     * @return
     */
    @PostMapping("/resend/person")
    boolean retrySendSimpleEmailToPerson(@RequestBody SimpleEmail simpleEmail, @RequestParam("currentRetryTimes") Integer currentRetryTimes, @RequestParam("messageId")String messageId);
}
