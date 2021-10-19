package com.pj.project4sp.article4digest;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("digest")
public class Digest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long digestId;

    private String weekId;

    private Long topicId;

    private String topicName;

    private String image;

    private LocalDateTime createTime;

    private List<Long> articleIds;

    private List<Long> subscribers;

    public Digest(Long topicId, String weekId, String topicName, String image, List<Long> articleIds, List<Long> subscribers) {
        Snowflake snowflake = IdUtil.getSnowflake(5);
        digestId = snowflake.nextId();
        this.weekId = weekId;
        this.topicId = topicId;
        this.topicName = topicName;
        this.image = image;
        createTime = LocalDateTime.now(ZoneOffset.ofHours(8));
        this.articleIds = articleIds;
        this.subscribers = subscribers;
    }
}
