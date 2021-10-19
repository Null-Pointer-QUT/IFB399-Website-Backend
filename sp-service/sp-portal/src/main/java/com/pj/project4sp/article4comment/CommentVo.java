package com.pj.project4sp.article4comment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pj.project4sp.user.SpUserUtil;
import com.pj.project4sp.user.SpUserVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commentId;

    /**
     *  ***
     */
    private SpUserVo user;

    private Long parentId;

    /**
     *  ***
     */
    private SpUserVo toUser;

    private String comment;

    private Integer nrOfMentioned;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public static List<List<CommentVo>> copyFromEntity(List<Comment> comments) {
        //id -> userId
        HashMap<Long, Long> comment2UserMap = new HashMap<>();
        HashMap<Long, Long> comment2ParentMap = new HashMap<>();
        List<CommentVo> commentVos = comments.stream()
                .peek(comment -> {
                    comment2UserMap.put(comment.getCommentId(), comment.getUserId());
                    comment2ParentMap.put(comment.getCommentId(), comment.getParentId());
                    if (comment.getParentId() != 0 && comment2ParentMap.get(comment.getParentId()) != 0) {
                        comment2ParentMap.put(comment.getCommentId(), comment2ParentMap.get(comment.getParentId()));
                    }
                })
                .map(comment -> {
                    CommentVo commentVo = CglibUtil.copy(comment, CommentVo.class);
                    commentVo.setUser(SpUserUtil.getUserById(comment.getUserId()));
                    if (comment.getParentId() != 0) {
                        commentVo.setToUser(SpUserUtil.getUserById(comment2UserMap.get(comment.getParentId())));
                    }
                    return commentVo;
                }).collect(Collectors.toList());
        //id -> commentList
        HashMap<Long, List<CommentVo>> commentId2commentListMap = new LinkedHashMap<>();
        commentVos.forEach(commentVo -> {
            if (commentVo.getParentId() == 0) {
                commentId2commentListMap.put(commentVo.getCommentId(), CollUtil.newArrayList(commentVo));
            } else {
                commentId2commentListMap.get(comment2ParentMap.get(commentVo.getCommentId())).add(commentVo);
            }

        });
        return CollUtil.newArrayList(commentId2commentListMap.values());
    }
}
