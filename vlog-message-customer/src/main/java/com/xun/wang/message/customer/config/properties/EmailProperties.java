package com.xun.wang.message.customer.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SimpleEmailProperties
 * @Description email 配置
 * @Author xun.d.wang
 * @Date 2019/12/4 13:20
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = "message.email")
public class EmailProperties {
    private String host;
    private String password;
    private String sender;
}
