package com.pj.project4sp.article4topic;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class TopicUpdateListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Id of topic cannot be empty")
    private Long topicId;

    @NotBlank(message = "Key of topic cannot be empty")
    private String key;

    @NotNull(message = "Value of topic cannot be empty")
    private List<String> value;
}
