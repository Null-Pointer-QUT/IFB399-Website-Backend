package com.pj.project4sp.user4feedback;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.cglib.CglibUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class FeedbackUploadParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String phone;

    private String subject;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    public Feedback copyToEntity() {
        Feedback feedback = CglibUtil.copy(this, Feedback.class);
        Snowflake snowflake = IdUtil.getSnowflake(4);
        feedback.setFeedbackId(snowflake.nextId());
        feedback.setCreateTime(LocalDateTime.now(ZoneOffset.ofHours(8)));
        if (StpUtil.isLogin()) {
            feedback.setUserId(StpUtil.getLoginIdAsLong());
        }
        return feedback;
    }
}
