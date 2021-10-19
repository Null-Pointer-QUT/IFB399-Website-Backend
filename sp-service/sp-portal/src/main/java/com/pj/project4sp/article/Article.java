package com.pj.project4sp.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @Id
    private Long articleId;

    /**
     * id of user
     */
    private Long userId;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 点赞数量
     */
    private List<Long> thumbUp;

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

    private List<Long> commentIds;

    private String indexImage;

    private List<String> tags;

}