package com.xun.wang.vlog.config.server.config;

import com.sun.nio.file.SensitivityWatchEventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;

/**
 * @ClassName ModifyFileEvent
 * @Description 配置监听器
 * @Author xun.d.wang
 * @Date 2019/12/5 11:23
 * @Version 1.0
 **/
@Configuration
public class ModifyFileEventListenerConfig {

    @Value("${properties.file.path}")
    private String propertiesFilePath;

    @Autowired
    private RestTemplateBuilder builder;


    @Bean
    public WatchService  modifyFileEventWatchService() throws IOException {
        //构造监听服务
        WatchService watcher = FileSystems.getDefault().newWatchService();
        //监听注册，监听实体的创建、修改、删除事件，并以高频率(每隔2秒一次，默认是10秒)监听
        Paths.get(propertiesFilePath).register(watcher,
                new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY},
                SensitivityWatchEventModifier.HIGH);
        return watcher;
    }

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

}
