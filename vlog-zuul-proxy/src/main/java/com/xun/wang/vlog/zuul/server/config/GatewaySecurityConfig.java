
package com.xun.wang.vlog.zuul.server.config;

import com.xun.wang.vlog.zuul.server.handler.GatewayAccessDeniedHandler;
import com.xun.wang.vlog.zuul.server.handler.GatewayAuditLogFilter;
import com.xun.wang.vlog.zuul.server.handler.GatewayAuthenticationEntryPoint;
import com.xun.wang.vlog.zuul.server.handler.GatewayWebSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;


@Configuration
@EnableResourceServer
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private GatewayWebSecurityExpressionHandler gatewayWebSecurityExpressionHandler;

    @Autowired
    private GatewayAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private GatewayAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("message")
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .expressionHandler(gatewayWebSecurityExpressionHandler);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new GatewayAuditLogFilter(), ExceptionTranslationFilter.class)
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .anyRequest().access("#permissionService.hasPermission(request, authentication)");
    }

}
