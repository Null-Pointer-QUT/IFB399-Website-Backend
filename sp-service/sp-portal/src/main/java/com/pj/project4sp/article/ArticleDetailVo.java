package com.pj.project4sp.article;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pj.project4sp.article4comment.CommentVo;
import com.pj.project4sp.user.SpUser;
import com.pj.project4sp.user.SpUserUtil;
import com.pj.project4sp.user.SpUserVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ArticleDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * user ***
     */
    private SpUserVo user;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 顶部图片
     */
    private String topImage;

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
     * 附件
     */
    private List<String> attachments;

    /**
     * 技术分类
     */
    private String technology;

    private String grade;

    private Boolean isPublish;

    /**
     * 评论 ***
     */
    private List<List<CommentVo>> commentList;

    private Integer nrOfComment;

    private String indexImage;

    private List<String> tags;

    public static ArticleDetailVo copyFromEntity(Article article) {
        ArticleDetailVo articleDetailVo = CglibUtil.copy(article, ArticleDetailVo.class);
        articleDetailVo.setUser(SpUserUtil.getUserById(article.getUserId()));
        articleDetailVo.setNrOfThumbUp(article.getThumbUp().size());
        articleDetailVo.setNrOfComment(article.getCommentIds().size());
        articleDetailVo.setIsThumbUp(false);
        if (StpUtil.isLogin()) {
            SpUser currUser = SpUserUtil.getCurrUser();
            Set<Long> thumbUpUserIds = new HashSet<>(article.getThumbUp());
            if (thumbUpUserIds.contains(currUser.getId())) {
                articleDetailVo.setIsThumbUp(true);
            }
        }
        articleDetailVo.setCommentList(new ArrayList<>());
        return articleDetailVo;
    }
}
