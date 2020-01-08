package com.xun.wang.vlog.common.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * @ClassName UserNameAuditorAware
 * @Description 设置当前操作人
 * @Author xun.d.wang
 * @Date 2019/12/31 16:49完成
 * @Version 1.0
 **/
@Component
public class UserNameAuditorAware implements AuditorAware<String> {


    private String userName = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(userName);
    }

}
