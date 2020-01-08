package com.xun.wang.vlog.zuul.server;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class VlogZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VlogZuulServerApplication.class, args);
    }

}
