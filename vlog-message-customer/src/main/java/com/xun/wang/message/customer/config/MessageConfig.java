package com.xun.wang.message.customer.config;

import com.xun.wang.message.customer.config.properties.EmailProperties;
import com.xun.wang.message.customer.config.properties.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName EmailConfig
 * @Description email配置
 * @Author xun.d.wang
 * @Date 2019/12/4 10:59
 * @Version 1.0
 **/
@Configuration
public class MessageConfig {


    @EnableConfigurationProperties(EmailProperties.class)
    public class EmailConfig{
        @Autowired
        private EmailProperties emailProperties;

        @Bean
        public JavaMailSenderImpl simpleSender() {
            JavaMailSenderImpl simpleSender = new JavaMailSenderImpl();
            simpleSender.setHost(emailProperties.getHost());
            simpleSender.setUsername(emailProperties.getSender());
            simpleSender.setPassword(emailProperties.getPassword());
            simpleSender.setDefaultEncoding("UTF-8");
            return simpleSender;
        }
    }

    @EnableConfigurationProperties(SmsProperties.class)
    public class SmsConfig{
        @Bean
        public RestTemplate restTemplate(ClientHttpRequestFactory factory){
            return new RestTemplate(factory);
        }

        @Bean
        public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(15000);
            factory.setReadTimeout(5000);
            return factory;
        }

    }




}
