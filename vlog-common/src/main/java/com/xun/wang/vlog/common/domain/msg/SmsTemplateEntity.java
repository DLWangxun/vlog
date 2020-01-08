package com.xun.wang.vlog.common.domain.msg;

import com.xun.wang.vlog.common.domain.BaseEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;

import javax.persistence.OneToMany;
import java.util.List;

/**
 * @ClassName SmsTemplateEntity
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/1/6 17:11
 * @Version 1.0
 **/
@Data
public class SmsTemplateEntity extends BaseEntity {

    @Column(columnDefinition="varchar(255) COMMENT '签名'")
    private String sign;


    @Column(columnDefinition="varchar(1000) COMMENT '模板内容'")
    private String templateContext;

    @OneToMany(mappedBy = "smsTemplate",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<SmsTemplateParam> params;

}
