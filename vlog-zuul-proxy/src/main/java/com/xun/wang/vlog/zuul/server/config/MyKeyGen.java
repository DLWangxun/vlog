package com.xun.wang.vlog.zuul.server.config;

import javax.servlet.http.HttpServletRequest;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.DefaultRateLimitKeyGenerator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.stereotype.Component;

/**
 * @ClassName MyKeyGen
 * @Description 自定义rate key值
 * @Author xun.d.wang
 * @Date 2020/1/28 13:42
 * @Version 1.0
 **/
@Component
public class MyKeyGen extends DefaultRateLimitKeyGenerator {

    public MyKeyGen(RateLimitProperties properties, RateLimitUtils rateLimitUtils) {
        super(properties, rateLimitUtils);
    }

    @Override
    public String key(HttpServletRequest request, Route route, RateLimitProperties.Policy policy) {
        return super.key(request, route, policy);
    }
}
