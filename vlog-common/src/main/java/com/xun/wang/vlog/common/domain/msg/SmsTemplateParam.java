package com.xun.wang.vlog.common.domain.msg;

import com.xun.wang.vlog.common.domain.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @ClassName SmsTemplateParam
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 17:25
 * @Version 1.0
 **/
public class SmsTemplateParam extends BaseEntity {

    @Column(name="[name]",nullable = false,columnDefinition="varchar(255) COMMENT '变量名称'")
    private String name;

    @Column(name="[value]",nullable = false,columnDefinition="tinyint COMMENT '变量值'")
    private Integer value;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="sms_template_id")
    private SmsTemplateEntity smsTemplate;
}
