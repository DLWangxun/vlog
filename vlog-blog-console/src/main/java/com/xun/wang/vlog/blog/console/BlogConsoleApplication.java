package com.xun.wang.vlog.blog.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// 服务发现
@EnableDiscoveryClient
@SpringBootApplication
public class BlogConsoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogConsoleApplication.class, args);
    }
}
