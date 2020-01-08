package com.xun.wang.message.customer.enums;

import lombok.Getter;

/**
 * @ClassName MsgTypeEnums
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/7 10:56
 * @Version 1.0
 **/
@Getter
public enum MsgTypeEnums {

    SMS("sms"),
    EMAIL("email");

    private String msgType;

    MsgTypeEnums(String msgType) {
        this.msgType = msgType;
    }
}
