package com.pj.project4sp.article;

import com.pj.project4sp.article4comment.CommentParam;

import java.util.List;

public interface IArticleService {

    Article uploadArticle(ArticleUploadParam uploadParam);

    List<ArticleIntroVo> getArticleList(String tag);

    List<ArticleIntroVo> getLikedArticleList();

    List<ArticleIntroVo> getMyArticleList(Boolean isPublish);

    List<ArticleIntroVo> getTopicArticleList(Long topicId);

    ArticleDetailVo getArticleDetail(Long articleId);

    String changePublicationStatus(Long articleId);

    void updateArticleDetail(ArticleUpdateParam param);

    void updateArticleDetailList(ArticleUpdateListParam param);

    void updateArticleAllDetail(ArticleUploadParam param);

    void deleteArticle(Article articleId);

    Long addComment(CommentParam commentParam);

    Boolean thumbUp(Long articleId);

    List<ArticleIntroVo> search(String keyword);

    void incDownload(Long articleId);
}
