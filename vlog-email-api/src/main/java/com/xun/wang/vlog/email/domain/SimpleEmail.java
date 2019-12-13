package com.xun.wang.vlog.email.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SimpleEmail
 * @Description 序列化简单邮件操作
 * @Author xun.d.wang
 * @Date 2019/12/3 14:17
 * @Version 1.0
 **/
@Data
public class SimpleEmail  implements Serializable {
    private static final long serialVersionUID = 8830258957473993741L;
    private String sender;
    private String reciever;
    private String subject;
    private String content;
}
