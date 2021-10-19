package com.pj.project4sp.article;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pj.project4sp.user.SpUser;
import com.pj.project4sp.user.SpUserUtil;
import com.pj.project4sp.user.SpUserVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ArticleIntroVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * id of user ***
     */
    private SpUserVo user;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 视频链接
     */
    private List<String> videoUrl;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 点赞数量 ***
     */
    private Integer nrOfThumbUp;

    /**
     * 是否点赞 ***
     */
    private Boolean isThumbUp;

    /**
     * 访客数量
     */
    private Integer nrOfVisit;

    /**
     * 下载量
     */
    private Integer nrOfDownloads;

    /**
     * 技术分类
     */
    private String technology;

    /**
     * 年级
     */
    private String grade;

    private Boolean isPublish;

    private String indexImage;

    private Integer nrOfComment;

    private List<String> tags;

    public static ArticleIntroVo copyFromEntity(Article article) {
        ArticleIntroVo articleIntroVo = CglibUtil.copy(article, ArticleIntroVo.class);
        articleIntroVo.setNrOfComment(article.getCommentIds().size());
        articleIntroVo.setUser(SpUserUtil.getUserById(article.getUserId()));
        articleIntroVo.setNrOfThumbUp(article.getThumbUp().size());
        articleIntroVo.setIsThumbUp(false);
        if (StpUtil.isLogin()) {
            SpUser currUser = SpUserUtil.getCurrUser();
            Set<Long> thumbUpUserIds = new HashSet<>(article.getThumbUp());
            if (thumbUpUserIds.contains(currUser.getId())) {
                articleIntroVo.setIsThumbUp(true);
            }
        }
        return articleIntroVo;
    }

}
