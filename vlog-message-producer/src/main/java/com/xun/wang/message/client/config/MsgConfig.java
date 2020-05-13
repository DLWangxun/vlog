package com.xun.wang.message.client.config;

import com.xun.wang.message.client.config.properties.MsgProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName EmailConfig
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/16 18:18
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(MsgProperties.class)
public class MsgConfig {


    @Autowired
    private MsgProperties msgProperties;


    /**
     * @ClassName SmsTemplateConfig
     * @Description TODO
     * @Author xun.d.wang
     * @Date 2019/12/10 14:53
     * @Version 1.0
     **/
    public class SmsTemplateConfig {



        /**
         * 声明工作交换机的名称
         *
         * @return
         */
        @Bean
        DirectExchange smsWorkExchange() {
            return new DirectExchange(msgProperties.getSmsTemplate().getWorkExchangeName());
        }

        /**
         * 声明工作队列
         *
         * @return
         */
        @Bean
        Queue smsWorkQueue() {
            Map<String, Object> args = new HashMap<>(2);
            //声明重试交换器
            args.put("x-dead-letter-exchange",msgProperties.getSmsTemplate().getFailExchangeName());
            //声明重试路由键
            args.put("x-dead-letter-routing-key", msgProperties.getSmsTemplate().getFailRoutingKey());
            return new Queue(msgProperties.getSmsTemplate().getWorkQueueName(), true, false, false, args);
        }

        @Bean
        Binding smsBindingWork() {
            return BindingBuilder.bind(smsWorkQueue()).to(smsWorkExchange()).with(msgProperties.getSmsTemplate().getWorkRoutingKey());
        }

        /**
         * 声明重试交换机的名称
         *
         * @return
         */
        @Bean
        DirectExchange smsRetryExchange() {
            return new DirectExchange(msgProperties.getSmsTemplate().getRetryExhangeName());
        }

        /**
         * 声明重试队列
         *
         * @return
         */
        @Bean
        Queue smsRetryQueue() {
            Map<String, Object> args = new HashMap<>(2);
            //声明重试交换器
            args.put("x-dead-letter-exchange", msgProperties.getSmsTemplate().getWorkExchangeName());
            //声明重试路由键
            args.put("x-dead-letter-routing-key", msgProperties.getSmsTemplate().getWorkRoutingKey());
            return new Queue(msgProperties.getSmsTemplate().getRetryQueueName(), true, false, false, args);
        }

        @Bean
        Binding smsBindingRetry() {
            return BindingBuilder.bind(smsRetryQueue()).to(smsRetryExchange()).with(msgProperties.getSmsTemplate().getRetryRoutingKey());
        }

        /**
         * 声明失败交换机的名称
         *
         * @return
         */
        @Bean
        DirectExchange smsFailExchange() {
            return new DirectExchange(msgProperties.getSmsTemplate().getFailExchangeName());
        }

        /**
         * 声明失败队列
         *
         * @return
         */
        @Bean
        Queue smsFailQueue() {
            return new Queue(msgProperties.getSmsTemplate().getFailQueueName(), true);
        }


        @Bean
        Binding templateBindingFail() {
            return BindingBuilder.bind(smsFailQueue()).to(smsFailExchange()).with(msgProperties.getSmsTemplate().getFailRoutingKey());
        }
    }

    /**
     * @ClassName TemplateEmailConfig
     * @Description TODO
     * @Author xun.d.wang
     * @Date 2019/12/10 14:53
     * @Version 1.0
     **/
    public class EmailTemplateConfig {



        /**
         * 声明工作交换机的名称
         *
         * @return
         */
        @Bean
        DirectExchange emailWorkExchange() {
            return new DirectExchange(msgProperties.getEmailTemplate().getWorkExchangeName());
        }

        /**
         * 声明工作队列
         *
         * @return
         */
        @Bean
        Queue emailWorkQueue() {
            Map<String, Object> args = new HashMap<>(2);
            //声明重试交换器
            args.put("x-dead-letter-exchange", msgProperties.getEmailTemplate().getFailExchangeName());
            //声明重试路由键
            args.put("x-dead-letter-routing-key", msgProperties.getEmailTemplate().getFailRoutingKey());
            return new Queue(msgProperties.getEmailTemplate().getWorkQueueName(), true, false, false, args);
        }

        @Bean
        Binding emailBindingWork() {
            return BindingBuilder.bind(emailWorkQueue()).to(emailWorkExchange()).with(msgProperties.getEmailTemplate().getWorkRoutingKey());
        }

        /**
         * 声明失败交换机的名称
         *
         * @return
         */
        @Bean
        DirectExchange emailFailExchange() {
            return new DirectExchange(msgProperties.getEmailTemplate().getFailExchangeName());
        }

        /**
         * 声明失败队列
         *
         * @return
         */
        @Bean
        Queue emailFailQueue() {
            return new Queue(msgProperties.getEmailTemplate().getFailQueueName(), true);
        }


        @Bean
        Binding emailBindingFail() {
            return BindingBuilder.bind(emailFailQueue()).to(emailFailExchange()).with(msgProperties.getEmailTemplate().getFailRoutingKey());
        }
    }
}
