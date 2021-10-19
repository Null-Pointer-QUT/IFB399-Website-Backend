package com.pj.project4sp.article;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ArticleUpdateParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Id of article cannot be empty")
    private Long articleId;

    private Boolean isListAdded;

    @NotBlank(message = "Key of article cannot be empty")
    private String key;

    @NotBlank(message = "value of article cannot be empty")
    private String value;
}
