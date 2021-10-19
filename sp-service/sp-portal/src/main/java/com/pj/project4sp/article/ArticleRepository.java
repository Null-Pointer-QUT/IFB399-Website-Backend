package com.pj.project4sp.article;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, Long> {

    List<Article> findArticlesByGradeOrderByCreateTime(String grade);

    List<Article> findArticlesByTechnologyOrderByCreateTime(String technology);

    List<Article> findArticlesByTechnologyAndGradeOrderByCreateTime(String technology, String grade);

    Article findArticleByArticleIdAndIsPublish(Long articleId, Boolean isPublish);

    List<Article> findArticlesByIsPublishAndTitleLikeOrContentLikeOrTagsLike(Boolean isPublish, String title, String content, String tags);
}
