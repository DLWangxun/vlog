package com.xun.wang.vlog.config.server.listener;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.sun.jmx.mbeanserver.Util.cast;


@Component
public class ModifyFileListener {

    @Autowired
    private WatchService modifyFileEventWatchService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;


    @PostConstruct
    public void init() {
        //异步起线程去执行，让这个bean加载完
        new Thread(() -> {
            while (true) {
                //每隔3秒拉取监听key
                WatchKey key = null;  //等待，超时就返回
                try {
                    key = modifyFileEventWatchService.poll(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //监听key为null,则跳过
                if (key == null) {
                    continue;
                }
                //获取监听事件
                for (WatchEvent<?> event : key.pollEvents()) {
                    //获取监听事件类型
                    WatchEvent.Kind kind = event.kind();
                    //异常事件跳过
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    String changeFilePath = cast(event.context()).toString();
                    //文件修改
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY && changeFilePath.endsWith("properties")) {
                        //获取文件名构造正则表达式：.*${configName}.*.
                        String regex = ".*".concat(StringUtils.substringBefore(changeFilePath, ".")).concat(".*");
                        List<String> serviceNames = discoveryClient.getServices();
                        List<String> refreshServiceNames = serviceNames.stream()
                                .filter(serviceName -> Pattern.matches(regex, serviceName))
                                .collect(Collectors.toList());
                        String refreshServiceName = refreshServiceNames.size() > 0 ? refreshServiceNames.get(0) : null;
                        //发布事件,/bus/refresh?destination={对应的服务}集群
                        if (StringUtils.isNotBlank(refreshServiceName)) {
                            //本地ip
                            String host = "localhost";
                            //本地端口
                            String port = environment.getProperty("server.port");
                            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
                            builder.scheme("http");
                            builder.host(host);
                            builder.port(port);
                            builder.path("/actuator/bus-refresh");
                            builder.query("destination={destination}");
                            URI uri = builder.build().expand(refreshServiceName.concat(":**")).encode().toUri();
                            ResponseEntity responseEntity = restTemplate.postForEntity(uri, null, boolean.class);
                        }
                        continue;
                    }
                }
                //处理监听key后(即处理监听事件后)，监听key需要复位，便于下次监听
                key.reset();
            }
        }).start();
    }


}
