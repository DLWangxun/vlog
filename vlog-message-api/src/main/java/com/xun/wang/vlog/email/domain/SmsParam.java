package com.xun.wang.vlog.email.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SmsParam
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 16:45
 * @Version 1.0
 **/
@Data
public class SmsParam implements Serializable {


    private static final long serialVersionUID = -8383748121809924482L;
    private String smsMob;
    private SmsTemplate smsTemplate;

}
