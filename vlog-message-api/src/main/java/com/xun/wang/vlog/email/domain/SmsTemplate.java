package com.xun.wang.vlog.email.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName SmsTemplate
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 17:02
 * @Version 1.0
 **/
@Data
public class SmsTemplate implements Serializable {


    private static final long serialVersionUID = 8450925058964319731L;
    private String sign;
    private String templateContext;
    private Map<String,String> params;

}
