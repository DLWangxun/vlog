package com.xun.wang.vlog.email.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName source
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2019/12/16 14:20
 * @Version 1.0
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source implements Serializable {

    private static final long serialVersionUID = -8552136683346076657L;
    private String name;
    private String sourceUrl;
}
