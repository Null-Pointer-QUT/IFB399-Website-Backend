package com.pj.project4sp.article4topic;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.cglib.CglibUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TopicIntroVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long topicId;

    private String topicName;

    private String image;

    private String introduction;

    private Boolean isPublic;

    private List<Long> relatedArticles;

    private Integer nrOfRelatedArticle;

    private Integer nrOfSubscriber;

    private Boolean isSubscribed;

    public static TopicIntroVo copyFromEntity(Topic topic) {
        long userId = 0L;
        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsLong();
        }
        TopicIntroVo introVo = CglibUtil.copy(topic, TopicIntroVo.class);
        introVo.setNrOfRelatedArticle(topic.getRelatedArticles().size());
        introVo.setNrOfSubscriber(topic.getSubscribers().size());
        introVo.setIsSubscribed(topic.getSubscribers().contains(userId));
        return introVo;
    }

}
