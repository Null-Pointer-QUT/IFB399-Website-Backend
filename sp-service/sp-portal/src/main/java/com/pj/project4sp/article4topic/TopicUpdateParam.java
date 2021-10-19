package com.pj.project4sp.article4topic;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TopicUpdateParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Id of topic cannot be empty")
    private Long topicId;

    private Boolean isListAdded;

    @NotBlank(message = "Key of topic cannot be empty")
    private String key;

    @NotBlank(message = "Value of topic cannot be empty")
    private String value;
}
