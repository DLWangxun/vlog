package com.xun.wang.message.client.holder;

import com.xun.wang.message.client.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @ClassName EmailServiceProcessorHolder
 * @Description 根据bussinessType和sendType查找具体执行的处理器
 * @Author xun.d.wang
 * @Date 2019/12/16 15:27
 * @Version 1.0
 **/
@Component
public class MessageServiceHolder {

    @Autowired
    private Map<String, MsgService> msgServices;

    /**
     * @param bussinessType
     * @return
     */
    public MsgService findmsgServiceByType(String bussinessType) {
        String name = bussinessType.toLowerCase().concat(MsgService.class.getSimpleName());
        MsgService msgService = msgServices.get(name);
        if (msgService == null) {
            //TODO 没有该处理器，应报异常
            return null;
        }
        return msgService;
    }

}
