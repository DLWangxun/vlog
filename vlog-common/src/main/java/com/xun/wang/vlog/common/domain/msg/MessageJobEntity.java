package com.xun.wang.vlog.common.domain.msg;

import com.xun.wang.vlog.common.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName message_job
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/30 16:13
 * @Version 1.0
 **/
@Entity
@Table(name="t_message_job")
@Data
public class MessageJobEntity extends BaseEntity {


    @Column(nullable = false, columnDefinition="varchar(255) COMMENT '发送者'")
    private String sender;

    @Column(nullable = false,columnDefinition="varchar(255) COMMENT '接收者(;分隔每条数据不超过200)'")
    private String reciever;

    @Column(nullable = false,columnDefinition="INTEGER COMMENT '批次标识'")
    private BigDecimal batchId;

    @Column(nullable = false,columnDefinition="dateTime COMMENT '发送时间'")
    private Date sendTime;

    @Column(nullable = false,columnDefinition="tinyint COMMENT '是否发送'")
    private Integer isSent;

    @OneToOne
    @JoinColumn(name = "email_template_id", referencedColumnName = "id")
    private EmailTemplateEntity templateEmailEntity;


}
