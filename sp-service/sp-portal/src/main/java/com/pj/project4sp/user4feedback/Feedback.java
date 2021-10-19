package com.pj.project4sp.user4feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("feedback")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long feedbackId;

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String subject;

    private String message;

    private LocalDateTime createTime;
}
