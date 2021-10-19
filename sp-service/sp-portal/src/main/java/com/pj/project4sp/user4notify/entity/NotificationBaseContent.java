package com.pj.project4sp.user4notify.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class NotificationBaseContent implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long articleId;

    protected Long userId;

    public NotificationBaseContent() {
    }

    public NotificationBaseContent(Long articleId, Long userId) {
        this.articleId = articleId;
        this.userId = userId;
    }

}
