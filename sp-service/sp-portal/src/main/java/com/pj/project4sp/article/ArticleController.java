package com.pj.project4sp.article;

import cn.dev33.satoken.stp.StpUtil;
import com.pj.current.satoken.AuthConst;
import com.pj.project4sp.article4comment.CommentParam;
import com.pj.utils.sg.AjaxJson;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleService articleService;

    @PostMapping("/uploadArticle")
    public AjaxJson uploadArticle(@RequestBody @Validated ArticleUploadParam uploadParam) {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(articleService.uploadArticle(uploadParam));
    }

    @GetMapping("/getArticleList")
    public AjaxJson getArticleList(@RequestParam(required = false, defaultValue = "") String tag) {
        return AjaxJson.getSuccessData(articleService.getArticleList(tag));
    }

    @GetMapping("/getLikedArticleList")
    public AjaxJson getLikedArticleList() {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(articleService.getLikedArticleList());
    }

    @GetMapping("/getMyArticleList")
    public AjaxJson getMyArticleList(@RequestParam(required = false) Boolean isPublish) {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(articleService.getMyArticleList(isPublish));
    }

    @GetMapping("/getArticleDetail")
    public AjaxJson getArticleDetail(@RequestParam Long articleId) {
        return AjaxJson.getSuccessData(articleService.getArticleDetail(articleId));
    }

    @GetMapping("/changePublicationStatus")
    public AjaxJson changePublicationStatus(@RequestParam Long articleId) {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccess(articleService.changePublicationStatus(articleId));
    }

    @PostMapping("/updateArticleDetail")
    public AjaxJson updateArticleDetail(@Validated @RequestBody ArticleUpdateParam param) {
        StpUtil.checkPermission(AuthConst.r11);
        articleService.updateArticleDetail(param);
        return AjaxJson.getSuccess("Article modified successfully!");
    }

    @PostMapping("/updateArticleDetailList")
    public AjaxJson updateArticleDetailList(@Validated @RequestBody ArticleUpdateListParam param) {
        StpUtil.checkPermission(AuthConst.r11);
        articleService.updateArticleDetailList(param);
        return AjaxJson.getSuccess("Article modified successfully!");
    }

    @PostMapping("/updateArticleAllDetail")
    public AjaxJson updateArticleAllDetail(@Validated @RequestBody ArticleUploadParam param) {
        StpUtil.checkPermission(AuthConst.r11);
        articleService.updateArticleAllDetail(param);
        return AjaxJson.getSuccess("Article modified successfully!");
    }

    @PostMapping("/deleteArticle")
    public AjaxJson deleteArticle(@RequestBody Article article) {
        StpUtil.checkPermission(AuthConst.r11);
        articleService.deleteArticle(article);
        return AjaxJson.getSuccess("Article deleted successfully!");
    }

    @PostMapping("/addComment")
    public AjaxJson addComment(@RequestBody CommentParam commentParam) {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(articleService.addComment(commentParam));
    }

    @GetMapping("/thumbUp")
    public AjaxJson thumbUp(@RequestParam Long articleId) {
        StpUtil.checkPermission(AuthConst.r11);
        return AjaxJson.getSuccessData(articleService.thumbUp(articleId));
    }

    @GetMapping("/search")
    public AjaxJson search(@RequestParam String keyword) {
        return AjaxJson.getSuccessData(articleService.search(keyword));
    }

    @GetMapping("/incDownload")
    public AjaxJson incDownload(@RequestParam Long articleId) {
        articleService.incDownload(articleId);
        return AjaxJson.getSuccess("Number of download increased successfully!");
    }
}
