package com.pj.project4sp.user4notify.vo;

import cn.hutool.extra.cglib.CglibUtil;
import com.pj.project4sp.user4notify.entity.NotificationBaseContent;
import com.pj.project4sp.user4notify.entity.NotificationCommentContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotifyCommentVo extends NotifyBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String commentId;

    private String parentId;


    private String title;

    private String username;

    private String atUserId;

    private String atUsername;

    private String comment;

    public static NotifyCommentVo fromEntity(NotificationBaseContent content) {
        NotificationCommentContent commentContent = (NotificationCommentContent) content;
        NotifyCommentVo commentVo = CglibUtil.copy(commentContent, NotifyCommentVo.class);
        commentVo.setArticleId(commentContent.getArticleId().toString());
        commentVo.setUserId(commentContent.getUserId().toString());
        commentVo.setCommentId(commentContent.getCommentId().toString());
        commentVo.setParentId(commentContent.getParentId().toString());
        return commentVo;
    }
}
