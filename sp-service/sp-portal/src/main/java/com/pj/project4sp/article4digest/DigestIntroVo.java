package com.pj.project4sp.article4digest;

import cn.hutool.extra.cglib.CglibUtil;
import com.pj.project4sp.article.ArticleIntroVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DigestIntroVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String weekId;

    private List<TopicVo> topics;

    @Data
    static class TopicVo implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long topicId;

        private String topicName;

        private String image;

        private List<ArticleIntroVo> articles;
    }

    public DigestIntroVo(String weekId, List<DigestDetail> digestDetails) {
        this.weekId = weekId;
        topics = digestDetails.stream()
                .map(digestDetail -> CglibUtil.copy(digestDetail, TopicVo.class))
                .collect(Collectors.toList());
    }
}
