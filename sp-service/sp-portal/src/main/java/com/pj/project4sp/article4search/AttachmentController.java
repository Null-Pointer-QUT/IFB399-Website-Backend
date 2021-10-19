package com.pj.project4sp.article4search;

import com.pj.utils.sg.AjaxJson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;

    @GetMapping("/search")
    public AjaxJson search(String keyword) {
        return AjaxJson.getSuccessData(attachmentService.search(keyword));
    }
}
