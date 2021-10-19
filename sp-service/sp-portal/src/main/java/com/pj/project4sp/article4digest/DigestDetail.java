package com.pj.project4sp.article4digest;

import com.pj.project4sp.article.ArticleIntroVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DigestDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long digestId;

    private String weekId;

    private Long topicId;

    private String topicName;

    private String image;

    private LocalDateTime createTime;

    private List<ArticleIntroVo> articles;

}
