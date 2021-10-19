package com.pj.project4sp.user4notify.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationThumbUpContent extends NotificationBaseContent implements Serializable {

    private static final long serialVersionUID = 1L;

    public NotificationThumbUpContent(Long articleId, Long userId) {
        super(articleId, userId);
    }
}
