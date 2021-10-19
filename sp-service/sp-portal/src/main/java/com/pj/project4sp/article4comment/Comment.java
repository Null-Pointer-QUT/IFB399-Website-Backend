package com.pj.project4sp.article4comment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long commentId;

    private Long userId;

    private Long parentId;

    private String comment;

    private Integer nrOfMentioned;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}