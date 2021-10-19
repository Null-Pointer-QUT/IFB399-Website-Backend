package com.pj.project4sp.article4topic;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("topic")
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long topicId;

    private String topicName;

    private String image;

    private String introduction;

    private Boolean isPublic;

    private List<Long> relatedArticles;

    private List<Long> subscribers;

    private List<String> subTopics;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Topic(){}

    public Topic(String topicName, List<Long> relatedArticles) {
        Snowflake snowflake = IdUtil.getSnowflake(3);
        topicId = snowflake.nextId();
        this.topicName = topicName;
        isPublic = Boolean.FALSE;
        this.relatedArticles = relatedArticles;
        subscribers = new ArrayList<>();
        subTopics = new ArrayList<>();
        createTime = LocalDateTime.now(ZoneOffset.ofHours(8));
        updateTime = createTime;
    }
}
