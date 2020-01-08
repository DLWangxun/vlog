package com.xun.wang.vlog.email.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;


@FeignClient(name = "${message.server.name}",
        path = "/message")
public interface IMessageApi {

    /**
     * 发送消息
     *
     * @param paramStr 请求体body
     * @param businessType
     * @return
     */
    @PostMapping("/{businessType}/send")
    boolean send(@RequestBody String paramStr, @PathVariable("businessType") String businessType);

    /**
     * 重试发送邮箱
     *
     * @param paramStr          请求体body
     * @param currentRetryTimes 当前发送次数
     * @param messageId         消息Id
     * @param businessType              重发类型
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/retry/send/{businessType}")
    public boolean reSend(@PathVariable("businessType") String businessType,
                          @RequestParam("currentRetryTimes") Integer currentRetryTimes,
                          @RequestParam("messageId") String messageId,
                          @RequestBody String paramStr) throws IOException;

}
