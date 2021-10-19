package com.pj.project4sp.user4notify.vo;

import cn.hutool.extra.cglib.CglibUtil;
import com.pj.project4sp.user4notify.entity.NotificationBaseContent;
import com.pj.project4sp.user4notify.entity.NotificationThumbUpContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotifyThumbUpVo extends NotifyBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String username;

    public static NotifyThumbUpVo fromEntity(NotificationBaseContent content) {
        NotificationThumbUpContent thumbUpContent = (NotificationThumbUpContent) content;
        NotifyThumbUpVo thumbUpVo = CglibUtil.copy(thumbUpContent, NotifyThumbUpVo.class);
        thumbUpVo.setArticleId(thumbUpContent.getArticleId().toString());
        thumbUpVo.setUserId(thumbUpContent.getUserId().toString());
        return thumbUpVo;
    }

}
