package com.pj.project4sp.user4feedback;

import com.pj.utils.sg.AjaxJson;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final IFeedbackService feedbackService;

    @PostMapping("/upload")
    public AjaxJson upload(@Validated @RequestBody FeedbackUploadParam param) {
        feedbackService.upload(param);
        return AjaxJson.getSuccess("Thank you for your feedback!");
    }
}
