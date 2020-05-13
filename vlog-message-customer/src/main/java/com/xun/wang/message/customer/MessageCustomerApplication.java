package com.xun.wang.message.customer;


import com.xun.wang.vlog.email.api.IMessageApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = IMessageApi.class)
@EnableRetry
public class MessageCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageCustomerApplication.class, args);
    }


}
