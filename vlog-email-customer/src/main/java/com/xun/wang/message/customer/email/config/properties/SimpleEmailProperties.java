package com.xun.wang.message.customer.email.config.properties;

import lombok.Data;

/**
 * @ClassName SimpleEmailProperties
 * @Description 简单邮件配置
 * @Author xun.d.wang
 * @Date 2019/12/4 14:46
 * @Version 1.0
 **/
@Data
public class SimpleEmailProperties {
    private String host;
    private String password;
    private String sender;
    private Integer retryTimes;
}
