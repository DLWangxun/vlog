package com.xun.wang.message.customer.email.config;

import com.xun.wang.message.customer.email.config.properties.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @ClassName EmailConfig
 * @Description email配置
 * @Author xun.d.wang
 * @Date 2019/12/4 10:59
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfig {

    @Autowired
    private EmailProperties emailProperties;
    @Bean
    public JavaMailSenderImpl simpleSender(){
        JavaMailSenderImpl simpleSender = new JavaMailSenderImpl();
        simpleSender.setHost(emailProperties.getSimple().getHost());
        simpleSender.setUsername(emailProperties.getSimple().getSender());
        simpleSender.setPassword(emailProperties.getSimple().getPassword());
        return simpleSender;
    }

}
