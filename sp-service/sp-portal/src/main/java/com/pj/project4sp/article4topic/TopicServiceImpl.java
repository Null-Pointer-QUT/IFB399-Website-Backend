package com.pj.project4sp.article4topic;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.pj.project4sp.article.Article;
import com.pj.project4sp.article.ArticleRepository;
import com.pj.utils.sg.AjaxError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements ITopicService{

    private final TopicRepository topicRepo;

    private final ArticleRepository articleRepo;

    private final MongoTemplate mongoTemplate;

    private final Set<String> MODIFIABLE_LIST_KEYS = CollUtil.newHashSet("subTopics");

    private final Set<String> MODIFIABLE_KEYS = CollUtil.newHashSet("image", "introduction");

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void collectTopic() {
        log.info("Scheduled Collection Start!");
        Map<String, List<Long>> topic2ArticleIds = new HashMap<>();
        List<Article> articles = articleRepo.findAll();
        articles.forEach(article -> {
            if (CollUtil.isEmpty(article.getTags())) return;
            Long articleId = article.getArticleId();
            article.getTags().forEach(tag -> {
                List<Long> articleIds = topic2ArticleIds.getOrDefault(tag, new ArrayList<>());
                articleIds.add(articleId);
                topic2ArticleIds.putIfAbsent(tag, articleIds);
            });
        });

        topic2ArticleIds.forEach((topicName, articleIds) -> topicRepo.findTopicByTopicName(topicName)
                .map(t -> {
                    //收集subTopics中的article
                    if (CollUtil.isNotEmpty(t.getSubTopics())) {
                        List<Long> subArticleIds = t.getSubTopics().stream()
                                .flatMap(subTopic -> topic2ArticleIds.getOrDefault(subTopic, new ArrayList<>()).stream())
                                .distinct()
                                .collect(Collectors.toList());
                        CollUtil.addAllIfNotContains(articleIds, subArticleIds);
                    }

                    //更新articleIds
                    Update update = new Update().set("relatedArticles", articleIds)
                            .set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8)));
                    mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(t.getTopicId())), update, Topic.class);
                    return t;
                })
                .orElseGet(() -> mongoTemplate.insert(new Topic(topicName, articleIds))));
    }

    @Override
    public Boolean changeIsPublic(Long topicId) {
        Topic topic = topicRepo.findById(topicId)
                .map(t -> {
                    mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(topicId)),
                            Update.update("isPublic", !t.getIsPublic()).set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8))), Topic.class);
                    return t;
                })
                .orElseThrow(() -> AjaxError.get("Topic Not Found!"));
        return !topic.getIsPublic();
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepo.findAll();
    }

    @Override
    public void changeTopicDetails(TopicUpdateParam param) {
        if (MODIFIABLE_LIST_KEYS.contains(param.getKey())) {
            changeTopicListDetails(param);
            return;
        }
        if (!MODIFIABLE_KEYS.contains(param.getKey())) throw AjaxError.get("This property cannot be changed!");
        topicRepo.findById(param.getTopicId()).orElseThrow(() -> AjaxError.get("Topic Not Found!"));
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(param.getTopicId())),
                Update.update(param.getKey(), param.getValue()).set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8))), Topic.class);
    }

    @Override
    public void changeTopicListDetail(TopicUpdateListParam param) {
        topicRepo.findById(param.getTopicId()).map(t -> {
            if (!MODIFIABLE_LIST_KEYS.contains(param.getKey())) throw AjaxError.get("This property cannot be changed!");

            mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(param.getTopicId())),
                    Update.update(param.getKey(), param.getValue().stream().distinct().collect(Collectors.toList()))
                            .set("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8))),
                    Topic.class);
            return t;
        }).orElseThrow(() -> AjaxError.get("Topic Not Found!"));
    }

    public void changeTopicListDetails(TopicUpdateParam param) {
        if (BeanUtil.isEmpty(param.getIsListAdded()))
            throw AjaxError.get("When the field is a list, 'isListAdded' cannot be empty");
        topicRepo.findById(param.getTopicId()).orElseThrow(() -> AjaxError.get("Topic Not Found!"));
        Update update = Update.update("updateTime", LocalDateTime.now(ZoneOffset.ofHours(8)));
        if (param.getIsListAdded()) {
            update.addToSet(param.getKey(), param.getValue());
        } else {
            update.pull(param.getKey(), param.getValue());
        }
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(param.getTopicId())), update, Topic.class);
    }

    @Override
    public List<TopicIntroVo> getPublicTopics() {
        List<Topic> topics = mongoTemplate.find(Query.query(Criteria.where("isPublic").is(Boolean.TRUE)), Topic.class);
        if (CollUtil.isEmpty(topics)) return new ArrayList<>();
        return topics.stream()
                .map(TopicIntroVo::copyFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean subscribe(Long topicId) {
        long userId = StpUtil.getLoginIdAsLong();
        Topic topic = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(topicId).and("isPublic").is(Boolean.TRUE)), Topic.class);
        return Optional.ofNullable(topic).map(t -> {
            if (topic.getSubscribers().contains(userId)) {
                mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(topicId)),
                        new Update().pull("subscribers", userId), Topic.class);
                return Boolean.FALSE;
            }
            mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(topicId)),
                    new Update().push("subscribers", userId), Topic.class);
            return Boolean.TRUE;
        }).orElseThrow(() -> AjaxError.get("Topic Not Found!"));
    }
}
