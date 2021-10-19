package com.pj.project4sp.user4notify.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class NotifyBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String articleId;

    protected String userId;

    public NotifyBaseVo() {
    }

}
