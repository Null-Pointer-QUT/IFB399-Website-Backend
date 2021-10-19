package com.pj.project4sp.article;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.pj.project4sp.article4comment.Comment;
import com.pj.project4sp.article4comment.CommentParam;
import com.pj.project4sp.article4comment.CommentRepository;
import com.pj.project4sp.article4comment.CommentVo;
import com.pj.project4sp.article4search.IAttachmentService;
import com.pj.project4sp.article4topic.Topic;
import com.pj.project4sp.article4topic.TopicRepository;
import com.pj.project4sp.user4notify.INotificationService;
import com.pj.project4sp.user4notify.entity.MessageType;
import com.pj.project4sp.user4notify.entity.NotificationCommentContent;
import com.pj.project4sp.user4notify.entity.NotificationThumbUpContent;
import com.pj.utils.sg.AjaxError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService{

    private final ArticleRepository articleRepo;

    private final TopicRepository topicRepo;

    private final MongoTemplate mongoTemplate;

    private final CommentRepository commentRepo;

    private final INotificationService notificationService;

    private final IAttachmentService attachmentService;

    private final HashSet<String> MODIFIABLE_KEYS =
            CollUtil.newHashSet("title", "topImage", "videoUrl", "content", "attachments", "indexImage", "tags");

    private final HashSet<String> MODIFIABLE_LIST_KEYS =
            CollUtil.newHashSet("videoUrl", "attachments", "tags");


    @Override
    public Article uploadArticle(ArticleUploadParam uploadParam) {
        Article article = uploadParam.copyToEntity();
        if (CollUtil.isNotEmpty(article.getAttachments())) {
            attachmentService.indexFile(article.getArticleId(), article.getAttachments(), article.getUserId(), article.getCreateTime());
        }
        articleRepo.save(article);
        return article;
    }

    @Override
    public List<ArticleIntroVo> getArticleList(String tag) {
        Criteria criteria = Criteria.where("isPublish").is(true);
        if (StrUtil.isNotBlank(tag)) {
            criteria.and("tags").is(tag);
        }
        Query query = new Query();
        query.addCriteria(criteria);
        List<Article> articles = mongoTemplate.find(query, Article.class);
        if (CollUtil.isEmpty(articles)) return new ArrayList<>();
        return articles.stream().map(ArticleIntroVo::copyFromEntity)
                .sorted(Comparator.comparing(ArticleIntroVo::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleIntroVo> getLikedArticleList() {
        long userId = StpUtil.getLoginIdAsLong();
        Criteria criteria = Criteria.where("isPublish").is(true).and("thumbUp").is(userId);
        Query query = new Query(criteria);
        List<Article> articles = mongoTemplate.find(query, Article.class);
        if (CollUtil.isEmpty(articles)) return new ArrayList<>();
        return articles.stream().map(ArticleIntroVo::copyFromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ArticleIntroVo> getMyArticleList(Boolean isPublish) {
        long userId = StpUtil.getLoginIdAsLong();
        Criteria criteria = Criteria.where("userId").is(userId);
        if (BeanUtil.isNotEmpty(isPublish)) {
            criteria.and("isPublish").is(isPublish);
        }
        List<Article> articles = mongoTemplate.find(Query.query(criteria), Article.class);
        if (CollUtil.isEmpty(articles)) return new ArrayList<>();
        return articles.stream().map(ArticleIntroVo::copyFromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ArticleIntroVo> getTopicArticleList(Long topicId) {
        Topic topic = topicRepo.findById(topicId).orElseThrow(() -> AjaxError.get("Topic Not Found!"));
        List<Topic> topics = mongoTemplate.find(Query.query(Criteria.where("topicCame").in(topic.getSubTopics())), Topic.class);
        topics.add(topic);
        List<Long> topicIds = topics.stream()
                .flatMap(t -> t.getRelatedArticles().stream())
                .distinct()
                .collect(Collectors.toList());
        List<Article> articles = mongoTemplate.find(Query.query(Criteria.where("_id").in(topicIds).and("isPublish").is(Boolean.TRUE)), Article.class);
        if (CollUtil.isEmpty(articles)) return new ArrayList<>();
        return articles.stream()
                .map(ArticleIntroVo::copyFromEntity)
                .sorted(Comparator.comparing(ArticleIntroVo::getUpdateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDetailVo getArticleDetail(Long articleId) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> AjaxError.get("Article Not Found"));
        Update update = new Update();
        update.inc("nrOfVisit", 1L);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(articleId)), update, Article.class);
        ArticleDetailVo articleDetailVo = ArticleDetailVo.copyFromEntity(article);
        if (CollUtil.isNotEmpty(article.getCommentIds())) {
            List<Comment> comments = CollUtil.newArrayList(commentRepo.findAllById(article.getCommentIds()));
            List<List<CommentVo>> commentList = CommentVo.copyFromEntity(comments);
            articleDetailVo.setCommentList(commentList);
        }
        return articleDetailVo;
    }

    @Override
    @SneakyThrows
    public Long addComment(CommentParam commentParam) {
        long userId = StpUtil.getLoginIdAsLong();
        Article article = articleRepo.findById(commentParam.getArticleId()).orElseThrow(() -> AjaxError.get("Article Not Found"));
        Comment comment = commentParam.copyToEntity();
        Update update = new Update();
        update.push("commentIds", comment.getCommentId());
        Comment parentComment = null;
        if (comment.getParentId() != 0) {
            parentComment = commentRepo.findById(comment.getParentId()).map(c -> {
                Update parentUpdate = new Update();
                parentUpdate.inc("nrOfMentioned", 1);
                mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(c.getCommentId())), parentUpdate, Comment.class);
                return c;
            }).orElseGet(() -> {
                comment.setParentId(0L);
                return null;
            });
        }
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(article.getArticleId())), update, Article.class);
        commentRepo.insert(comment);

        notificationService.wsNotify(article.getUserId(), MessageType.COMMENT, new NotificationCommentContent(article.getArticleId(), userId, comment.getCommentId(), comment.getParentId()));
        if (parentComment != null) {
            notificationService.wsNotify(parentComment.getUserId(), MessageType.COMMENT, new NotificationCommentContent(article.getArticleId(), userId, comment.getCommentId(), comment.getParentId()));
        }
        return comment.getCommentId();
    }

    @Override
    @SneakyThrows
    public Boolean thumbUp(Long articleId) {
        long userId = StpUtil.getLoginIdAsLong();
        Article article = articleRepo.findById(articleId).orElseThrow(() -> AjaxError.get("Article Not Found"));
        if (article.getThumbUp().contains(userId)) {
            Update update = new Update();
            update.pull("thumbUp", userId);
            mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(articleId)), update, Article.class);
            return false;
        }
        Update update = new Update();
        update.push("thumbUp", userId);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(articleId)), update, Article.class);

        notificationService.wsNotify(article.getUserId(), MessageType.THUMB_UP, new NotificationThumbUpContent(articleId, userId));
        return true;
    }

    @Override
    public List<ArticleIntroVo> search(String keyword) {
        List<Article> articles = articleRepo.findArticlesByIsPublishAndTitleLikeOrContentLikeOrTagsLike(true, keyword, keyword, keyword);
        if (CollUtil.isEmpty(articles)) return new ArrayList<>();
        return articles.stream().map(ArticleIntroVo::copyFromEntity).collect(Collectors.toList());
    }

    @Override
    public void incDownload(Long articleId) {
        articleRepo.findById(articleId).orElseThrow(() -> AjaxError.get("Article Not Found"));
        Update update = new Update();
        update.inc("nrOfDownloads", 1);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(articleId)), update, Article.class);
    }

    @Override
    public String changePublicationStatus(Long articleId) {
        long userId = StpUtil.getLoginIdAsLong();
        Article article = articleRepo.findById(articleId).map(a -> {
            if (userId != a.getUserId()) {
                throw AjaxError.get("No permission, because you are not the author of the article!");
            }
            return a;
        }).orElseThrow(() -> AjaxError.get("Article Not Found!"));
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(articleId)),
                Update.update("isPublish", !article.getIsPublish()).set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8))), Article.class);
        return "The article has been " + (article.getIsPublish()?"private":"public");
    }

    @Deprecated
    @Override
    public void updateArticleDetail(ArticleUpdateParam param) {
        long userId = StpUtil.getLoginIdAsLong();
        articleRepo.findById(param.getArticleId()).map(a -> {
            if (userId != a.getUserId()) {
                throw AjaxError.get("No permission, because you are not the author of the article!");
            }
            return a;
        }).orElseThrow(() -> AjaxError.get("Article Not Found!"));

        if (MODIFIABLE_LIST_KEYS.contains(param.getKey())) {
            updateArticleListDetail(param);
            return;
        }
        if (!MODIFIABLE_KEYS.contains(param.getKey())) throw AjaxError.get("This property cannot be changed!");
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(param.getArticleId())),
                Update.update(param.getKey(), param.getValue()).set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8))), Article.class);
    }

    @Deprecated
    @Override
    public void updateArticleDetailList(ArticleUpdateListParam param) {
        long userId = StpUtil.getLoginIdAsLong();
        articleRepo.findById(param.getArticleId()).map(a -> {
            if (userId != a.getUserId()) {
                throw AjaxError.get("No permission, because you are not the author of the article!");
            }
            if (!MODIFIABLE_LIST_KEYS.contains(param.getKey())) throw AjaxError.get("This property cannot be changed!");

            mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(param.getArticleId())),
                    Update.update(param.getKey(), param.getValue().stream().distinct().collect(Collectors.toList()))
                            .set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8))),
                    Article.class);
            return a;
        }).orElseThrow(() -> AjaxError.get("Article Not Found!"));
    }

    @Override
    public void updateArticleAllDetail(ArticleUploadParam param) {
        long userId = StpUtil.getLoginIdAsLong();
        LocalDateTime now = LocalDateTime.now();
        if (BeanUtil.isEmpty(param.getArticleId())) throw AjaxError.get("articleId cannot be empty");
        articleRepo.findById(param.getArticleId()).map(a -> {
            if (userId != a.getUserId()) {
                throw AjaxError.get("No permission, because you are not the author of the article!");
            }

            List<String> removedAttachments = a.getAttachments().stream()
                    .filter(attach -> !param.getAttachments().contains(attach))
                    .collect(Collectors.toList());
            List<String> addedAttachments = param.getAttachments().stream()
                    .filter(attach -> !a.getAttachments().contains(attach))
                    .distinct()
                    .collect(Collectors.toList());

            if (CollUtil.isNotEmpty(addedAttachments)) {
                attachmentService.indexFile(param.getArticleId(), addedAttachments, userId, now);
            }
            if (CollUtil.isNotEmpty(removedAttachments)) {
                attachmentService.deleteFile(removedAttachments);
            }

            CglibUtil.copy(param, a);
            a.setUpdateTime(LocalDateTime.now(ZoneOffset.ofHours(8)));
            a.setVideoUrl(param.getVideoUrl().stream().distinct().collect(Collectors.toList()));
            a.setAttachments(param.getAttachments().stream().distinct().collect(Collectors.toList()));
            a.setTags(param.getTags().stream().distinct().collect(Collectors.toList()));
            a.setUpdateTime(now);

            articleRepo.save(a);
            return a;
        }).orElseThrow(() -> AjaxError.get("Article Not Found!"));
    }

    @Override
    public void deleteArticle(Article article) {
        long userId = StpUtil.getLoginIdAsLong();
        if (BeanUtil.isEmpty(article.getArticleId())) throw AjaxError.get("articleId cannot be empty");
        articleRepo.findById(article.getArticleId()).map(a -> {
            if (userId != a.getUserId()) {
                throw AjaxError.get("No permission, because you are not the author of the article!");
            }
            articleRepo.deleteById(article.getArticleId());
            return a;
        }).orElseThrow(() -> AjaxError.get("Article Not Found!"));
    }

    @Deprecated
    public void updateArticleListDetail(ArticleUpdateParam param) {
        if (BeanUtil.isEmpty(param.getIsListAdded()))
            throw AjaxError.get("When the field is a list, 'isListAdded' cannot be empty");
        Update update = Update.update("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8)));
        if (param.getIsListAdded()) {
            update.addToSet(param.getKey(), param.getValue());
        } else {
            update.pull(param.getKey(), param.getValue());
        }
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(param.getArticleId())), update, Article.class);
    }


}
