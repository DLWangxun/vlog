package com.xun.wang.message.client.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xun.wang.message.client.holder.MessageServiceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @ClassName sendToPerson
 * @Description 消息发送给个人
 * @Author xun.d.wang
 * @Date 2019/12/3 11:22
 * @Version 1.0
 **/

@RestController
@RequestMapping("/message")
public class SendMsgController {

    @Autowired
    private MessageServiceHolder messageServiceHolder;

    @Autowired
    private OAuth2RestTemplate restTemplate;


    /**
     * 异步发送邮箱
     *
     * @param paramStr 请求体body
     * @param businessType  业务类型（sms/email）
     * @retursms
     * @throws JsonProcessingException
     */
    @PostMapping("/{businessType}/send")
    public boolean send(@RequestBody String paramStr, @PathVariable("businessType") String businessType) throws IOException {
        // 发送消息
        return messageServiceHolder.findmsgServiceByType(businessType).sendProccess(paramStr);
    }

    /**
     * 重试发送邮箱
     * @param paramStr 请求体body
     * @param currentRetryTimes 当前发送次数
     * @param messageId 消息Id
     * @param businessType 重发类型
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/retry/send/{businessType}")
    public boolean reSend(@PathVariable("businessType") String businessType,
                          @RequestParam("currentRetryTimes") Integer currentRetryTimes,
                          @RequestParam("messageId") String messageId,
                          @RequestBody String paramStr) throws IOException{
        // 重发消息
        return  messageServiceHolder.findmsgServiceByType(businessType).retrySendProccess(paramStr,currentRetryTimes,messageId);
    }

    @GetMapping("/user")
    //@PreAuthorize("#oauth2.hasScope('read')")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SentinelResource("getUser")
    public String getUser(@AuthenticationPrincipal String username ){
        return username;
    }


}
