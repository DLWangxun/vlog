package com.xun.wang.vlog.email.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SimpleEmail
 * @Description 序列化简单邮件操作
 * @Author xun.d.wang
 * @Date 2019/12/3 14:17
 * @Version 1.0
 **/
@Data
public class EmailTemplate  implements Serializable {

    private static final long serialVersionUID = 8830258957473993741L;
    private String templateUrl;
    private Source[] sources;
    private Map<String,String> content;
}
