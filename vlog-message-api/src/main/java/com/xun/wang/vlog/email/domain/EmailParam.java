package com.xun.wang.vlog.email.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xun.wang.vlog.email.domain.EmailTemplate;
import lombok.Data;
import java.io.Serializable;

/**
 * @ClassName EmailParam
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/16 14:17
 * @Version 1.0
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailParam implements Serializable {

    private static final long serialVersionUID = -5848378490009505761L;
    private String sender;
    private String[] reciever;
    private String name;
    private String subject;
    private EmailTemplate template;

}
