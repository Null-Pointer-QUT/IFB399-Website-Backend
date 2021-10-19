package com.pj.project4sp.article;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.cglib.CglibUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleUploadParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long articleId;

    /**
     * 文章标题
     */
    @NotBlank(message = "Title of article cannot be empty")
    private String title;

    /**
     * 顶部图片
     */
    @NotBlank(message = "Top image of article cannot be empty")
    private String topImage;

    /**
     * 视频链接
     */
    private List<String> videoUrl;

    /**
     * 文章内容
     */
    @NotBlank(message = "Content of article cannot be empty")
    private String content;

    /**
     * 附件
     */
    private List<String> attachments;

    /**
     * 技术分类
     */
//    @NotBlank(message = "Technology of article cannot be empty")
    private String technology;

//    @NotBlank(message = "Grade of article cannot be empty")
    private String grade;

    @NotNull(message = "Status 'isPublish' of article cannot be empty")
    private Boolean isPublish;

    private String indexImage;

    private List<String> tags;

    public Article copyToEntity() {
        Article article = CglibUtil.copy(this, Article.class);
        Snowflake snowflake = IdUtil.getSnowflake(1);
        article.setArticleId(snowflake.nextId());
        long userId = StpUtil.getLoginIdAsLong();
        article.setUserId(userId);
        article.setCreateTime(LocalDateTime.now(ZoneOffset.ofHours(8)));
        article.setUpdateTime(article.getCreateTime());
        article.setThumbUp(new ArrayList<>());
        article.setNrOfVisit(0);
        article.setNrOfDownloads(0);
        article.setCommentIds(new ArrayList<>());
        if (CollUtil.isEmpty(this.tags)) this.tags = new ArrayList<>();
        article.setTags(new ArrayList<>(CollUtil.newHashSet(this.tags)));
        return article;
    }
}
