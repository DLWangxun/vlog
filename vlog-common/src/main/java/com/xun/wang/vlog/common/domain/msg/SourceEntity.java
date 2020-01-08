package com.xun.wang.vlog.common.domain.msg;

import com.xun.wang.vlog.common.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName source
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/16 14:20
 * @Version 1.0
 **/
@Data
@Entity
@Table(name="t_source")
public class SourceEntity  extends BaseEntity {

    @Column(name="[name]",nullable = false,columnDefinition="varchar(255) COMMENT '资源名称'")
    private String name;

    @Column(nullable = false,columnDefinition="varchar(255) COMMENT '资源地址'")
    private String sourceUrl;

    @Column(name="[order]",nullable = false,columnDefinition="tinyint COMMENT '资源顺序'")
    private Integer order;


}
