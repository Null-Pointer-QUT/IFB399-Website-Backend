package com.pj.project4sp.article;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ArticleUpdateListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Id of article cannot be empty")
    private Long articleId;

    @NotBlank(message = "Key of article cannot be empty")
    private String key;

    @NotNull(message = "value of article cannot be empty")
    private List<String> value;
}
