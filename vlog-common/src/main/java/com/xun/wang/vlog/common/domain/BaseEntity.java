package com.xun.wang.vlog.common.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName EmailParam
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/16 14:17
 * @Version 1.0
 **/
@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "effective",nullable = false, columnDefinition="TINYINT COMMENT '是否有效'")
    private BigDecimal effective;

    @CreatedDate
    @Column(name = "cdate", columnDefinition="datetime COMMENT '创建日期'")
    private Date cdate;

    @LastModifiedDate
    @Column(name = "edate", columnDefinition="datetime COMMENT '更新日期'")
    private Date edate;

    @CreatedBy
    @Column(name = "creator", columnDefinition="varchar(255) COMMENT '创建人'")
    private String creator;

    @LastModifiedBy
    @Column(name = "editor", columnDefinition="varchar(255) COMMENT '更新人'")
    private String editor;

}
