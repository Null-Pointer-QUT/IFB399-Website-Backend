package com.pj.project4sp.article4search;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AttachmentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileId;

    private Long articleId;

    private String fileName;

    private String url;

    private Long userId;

    private Long uploadTime;

    private String type;

    private List<String> contents;

    public AttachmentVo(){}



}
