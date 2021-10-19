package com.pj.project4sp.article4comment;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.cglib.CglibUtil;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class CommentParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long articleId;

    private Long parentId;

    private String comment;

    public Comment copyToEntity() {
        Comment comment = CglibUtil.copy(this, Comment.class);
        Snowflake snowflake = IdUtil.getSnowflake(2);
        comment.setCommentId(snowflake.nextId());
        comment.setCreateTime(LocalDateTime.now(ZoneOffset.ofHours(8)));
        comment.setNrOfMentioned(0);
        comment.setUserId(StpUtil.getLoginIdAsLong());
        return comment;
    }

}
