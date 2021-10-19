package com.pj.project4sp.user4notify.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationCommentContent extends NotificationBaseContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commentId;

    private Long parentId;

    public NotificationCommentContent(Long articleId, Long userId, Long commentId, Long parentId) {
        super(articleId, userId);
        this.commentId = commentId;
        this.parentId = parentId;
    }
}
