package com.xun.wang.vlog.common.domain.msg;

import com.xun.wang.vlog.common.domain.BaseEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @ClassName SimpleEmail
 * @Description 序列化简单邮件操作
 * @Author xun.d.wang
 * @Date 2019/12/3 14:17
 * @Version 1.0
 **/
@Data
@Entity
@Table(name="t_template_email")
public class EmailTemplateEntity extends BaseEntity {

    @Column(columnDefinition="varchar(255) COMMENT '模板类型'")
    private String name;

    @Column(columnDefinition="varchar(255) COMMENT '模板地址'")
    private String templateUrl;

    @ManyToMany(cascade= {CascadeType.REFRESH,CascadeType.MERGE},fetch= FetchType.LAZY)
    @JoinTable(name = "t_template_email_source",
            joinColumns = @JoinColumn(name="template_email_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "source_id",referencedColumnName = "id"))
    private List<SourceEntity> sources;
}
