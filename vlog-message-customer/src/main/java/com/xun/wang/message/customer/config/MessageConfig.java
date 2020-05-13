package com.xun.wang.message.customer.config;

import com.rabbitmq.client.Channel;
import com.xun.wang.message.customer.config.properties.EmailProperties;
import com.xun.wang.message.customer.config.properties.RetryProperties;
import com.xun.wang.message.customer.config.properties.SmsProperties;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.UUID;

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
    public class EmailConfig {

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
    public class SmsConfig {
        @Bean
        public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
            return new RestTemplate(factory);
        }

        @Bean
        public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(15000);
            factory.setReadTimeout(5000);
            return factory;
        }
    }

    @EnableConfigurationProperties(RetryProperties.class)
    public class RetryConfig {

        @Autowired
        private RetryProperties retryProperties;

        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory ) {
            SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
            containerFactory.setConnectionFactory(connectionFactory);
            containerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
            containerFactory.setChannelTransacted(true);
            containerFactory.setAdviceChain(
                    RetryInterceptorBuilder
                            .stateless()
                            .recoverer(new RejectAndDontRequeueRecoverer())
                            .retryOperations(retryTemplate())
                            .build()
            );
            return containerFactory;
        }



        @Bean
        public RetryTemplate retryTemplate() {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setBackOffPolicy(backOffPolicy());
            retryTemplate.setRetryPolicy(retryPolicy());
            return retryTemplate;
        }

        @Bean
        public ExponentialBackOffPolicy backOffPolicy() {
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(retryProperties.getBackOff().getInterval());
            backOffPolicy.setMultiplier(retryProperties.getBackOff().getMultiplier());
            backOffPolicy.setMaxInterval(retryProperties.getBackOff().getMaxInterval());
            return backOffPolicy;
        }

        @Bean
        public SimpleRetryPolicy retryPolicy() {
            SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
            //retryPolicy.setMaxAttempts(retryProperties.getPolicy().getMaxAttempts());
            return retryPolicy;
        }


    }



}
