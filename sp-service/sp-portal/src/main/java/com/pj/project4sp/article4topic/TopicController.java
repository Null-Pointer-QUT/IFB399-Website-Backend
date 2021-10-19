package com.pj.project4sp.article4topic;

import cn.dev33.satoken.stp.StpUtil;
import com.pj.current.satoken.AuthConst;
import com.pj.project4sp.article.IArticleService;
import com.pj.utils.sg.AjaxJson;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final ITopicService topicService;

    private final IArticleService articleService;

    @PostMapping("/manage/collectTopic")
    public AjaxJson collectTopic() {
        StpUtil.checkPermission(AuthConst.r1);
        topicService.collectTopic();
        return AjaxJson.getSuccess("Topic collection successfully!");
    }

    @GetMapping("/manage/changeIsPublic")
    public AjaxJson changeIsPublic(Long topicId) {
        StpUtil.checkPermission(AuthConst.r1);
        Boolean isPublic = topicService.changeIsPublic(topicId);
        return AjaxJson.getSuccess("The Topic has been " + (isPublic?"public":"private"));
    }

    @GetMapping("/manage/getAllTopics")
    public AjaxJson getAllTopics() {
        StpUtil.checkPermission(AuthConst.r1);
        return AjaxJson.getSuccessData(topicService.getAllTopics());
    }

    @PostMapping("/manage/changeTopicDetails")
    public AjaxJson changeTopicDetails(@Validated @RequestBody TopicUpdateParam param) {
        StpUtil.checkPermission(AuthConst.r1);
        topicService.changeTopicDetails(param);
        return AjaxJson.getSuccess("Topic modified successfully!");
    }

    @PostMapping("/manage/changeTopicListDetail")
    public AjaxJson changeTopicListDetail(@Validated @RequestBody TopicUpdateListParam param) {
        StpUtil.checkPermission(AuthConst.r1);
        topicService.changeTopicListDetail(param);
        return AjaxJson.getSuccess("Topic modified successfully!");
    }

    @GetMapping("/getPublicTopics")
    public AjaxJson getPublicTopics() {
        return AjaxJson.getSuccessData(topicService.getPublicTopics());
    }

    @GetMapping("/subscribe")
    public AjaxJson subscribe(@RequestParam Long topicId) {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccess(topicService.subscribe(topicId)?"Subscribe":"Unsubscribe" + " Successfully!");
    }

    @GetMapping("/explore")
    public AjaxJson explore(@RequestParam Long topicId) {
        return AjaxJson.getSuccessData(articleService.getTopicArticleList(topicId));
    }
}
