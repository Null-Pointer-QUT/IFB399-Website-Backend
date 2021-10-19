package com.pj.project4sp.article4digest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.extra.mail.MailUtil;
import com.pj.project4sp.article.Article;
import com.pj.project4sp.article.ArticleIntroVo;
import com.pj.project4sp.article4topic.Topic;
import com.pj.project4sp.user.SpUserUtil;
import com.pj.project4sp.user.SpUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DigestServiceImpl implements IDigestService{

    private final DigestRepository digestRepo;

    private final MongoTemplate mongoTemplate;

    @Override
    public void collectDigest(LocalDate localDate) {
        log.info("Scheduled Digest Collection Start!");
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        LocalDateTime startOfWeek = localDate.minusDays(dayOfWeek - 1).atStartOfDay();
        LocalDateTime endOfWeek = localDate.plusDays(8 - dayOfWeek).atStartOfDay().minusSeconds(1);
        List<Topic> topics = mongoTemplate.find(Query.query(Criteria.where("isPublic").is(Boolean.TRUE)), Topic.class);

        mongoTemplate.remove(Query.query(Criteria.where("weekId").is(localDate.minusDays(dayOfWeek - 1).toString())), Digest.class);

        topics.parallelStream().forEach(topic -> {
            List<Topic> subTopics = mongoTemplate.find(Query.query(Criteria.where("_id").in(topic.getSubTopics())), Topic.class);
            if (CollUtil.isNotEmpty(subTopics)) {
                List<Long> subArticleIds = subTopics.stream()
                        .flatMap(t -> t.getRelatedArticles().stream())
                        .collect(Collectors.toList());
                CollUtil.addAllIfNotContains(topic.getRelatedArticles(), subArticleIds);
            }
            List<Long> articleIds = mongoTemplate.find(Query.query(Criteria.where("_id").in(topic.getRelatedArticles())
                            .and("isPublish").is(Boolean.TRUE)
                            .andOperator(Criteria.where("createTime").gte(startOfWeek),
                                    Criteria.where("createTime").lte(endOfWeek))), Article.class)
                    .stream()
                    .map(Article::getArticleId)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(articleIds)) {
                Digest digest = new Digest(topic.getTopicId(), localDate.minusDays(dayOfWeek - 1).toString(), topic.getTopicName(), topic.getImage(), articleIds, topic.getSubscribers());
                digestRepo.insert(digest);
            }
        });
    }

    @Scheduled(cron = "0 30 23 ? * 1")
    public void collectDigestAutomatically() {
        LocalDate now = LocalDate.now(ZoneOffset.ofHours(8));
        collectDigest(now);
    }

    @Override
    public List<DigestIntroVo> getDigest(long userId) {
        List<Long> topicIds = mongoTemplate.find(Query.query(Criteria.where("subscribers").is(userId).and("isPublic").is(Boolean.TRUE)), Topic.class)
                .stream()
                .map(Topic::getTopicId)
                .collect(Collectors.toList());
        List<Digest> digests = mongoTemplate.find(Query.query(Criteria.where("topicId").in(topicIds)), Digest.class);
        Map<String, List<DigestDetail>> weekId2DigestMap = new LinkedHashMap<>();
        digests.stream()
                .sorted(Comparator.comparing(Digest::getDigestId).reversed())
                .map(digest -> {
                    DigestDetail digestDetail = CglibUtil.copy(digest, DigestDetail.class);
                    List<ArticleIntroVo> articleIntroVos = mongoTemplate.find(Query.query(Criteria.where("_id").in(digest.getArticleIds())
                            .and("isPublish").is(Boolean.TRUE)), Article.class)
                            .stream()
                            .map(ArticleIntroVo::copyFromEntity)
                            .collect(Collectors.toList());
                    digestDetail.setArticles(articleIntroVos);
                    return digestDetail;
                })
                .filter(digestDetail -> CollUtil.isNotEmpty(digestDetail.getArticles()))
                .forEach(digest -> {
                    List<DigestDetail> digestList = weekId2DigestMap.getOrDefault(digest.getWeekId(), new ArrayList<>());
                    digestList.add(digest);
                    weekId2DigestMap.putIfAbsent(digest.getWeekId(), digestList);
                });

        List<DigestIntroVo> digestIntroVos = new ArrayList<>();
        weekId2DigestMap.forEach((weekId, digestList) -> {
            DigestIntroVo digestIntroVo = new DigestIntroVo(weekId, digestList);
            digestIntroVos.add(digestIntroVo);
        });

        return digestIntroVos;
    }

    @Override
    public void sendEmail(String lastWeekId) {
        List<Digest> lastWeekDigests = mongoTemplate.find(Query.query(Criteria.where("weekId").is(lastWeekId)), Digest.class);
        List<Long> subscriberIds = lastWeekDigests.stream()
                .flatMap(digest -> digest.getSubscribers().stream())
                .distinct()
                .collect(Collectors.toList());
        System.out.println(subscriberIds);
        subscriberIds.forEach(subscriberId -> {
            List<Digest> digests = lastWeekDigests.stream()
                    .filter(digest -> digest.getSubscribers().contains(subscriberId))
                    .collect(Collectors.toList());
            List<DigestDetail> digestDetails = digests.stream()
                    .sorted(Comparator.comparing(Digest::getDigestId).reversed())
                    .map(digest -> {
                        DigestDetail digestDetail = CglibUtil.copy(digest, DigestDetail.class);
                        List<ArticleIntroVo> articleIntroVos = mongoTemplate.find(Query.query(Criteria.where("_id").in(digest.getArticleIds())
                                        .and("isPublish").is(Boolean.TRUE)), Article.class)
                                .stream().limit(5)
                                .map(a -> CglibUtil.copy(a, ArticleIntroVo.class))
                                .collect(Collectors.toList());
                        digestDetail.setArticles(articleIntroVos);
                        return digestDetail;
                    }).collect(Collectors.toList());
            String email = "";
            for (DigestDetail digestDetail: digestDetails) {
                List<ArticleIntroVo> articles = digestDetail.getArticles();
                StringBuilder articleListStr = new StringBuilder();
                for (ArticleIntroVo articleIntroVo: articles) {
                    articleListStr.append(String.format(EmailConstant.iEmailArticleContainer,
                            String.format(EmailConstant.iEmailArticleBodyContainer, articleIntroVo.getTitle(), articleIntroVo.getIndexImage(), articleIntroVo.getContent()) +
                                    String.format(EmailConstant.iEmailArticleReadMoreContainer, articleIntroVo.getArticleId().toString())));
                }
                email += String.format(EmailConstant.iEmailTopicContainer, String.format(EmailConstant.iEmailTopicNameStr, digestDetail.getTopicName()) + articleListStr);
            }
            email = String.format(EmailConstant.iEmailContainer, String.format(EmailConstant.iWeekIdStr, lastWeekId) + email);
            log.info(email);

            SpUserVo user = SpUserUtil.getUserById(subscriberId);
            MailUtil.send(user.getEmail(), "Null Pointer Weekly Digest", email, true);
            System.out.println(email);
            System.out.println(user.getEmail());
        });
    }

    @Scheduled(cron = "0 0 8 ? * 2")
    public void sendEmailAutomatically() {
        LocalDate lastWeek = LocalDate.now(ZoneOffset.ofHours(8)).minusDays(7);
        int dayOfWeek = lastWeek.getDayOfWeek().getValue();
        String lastWeekId = lastWeek.minusDays(dayOfWeek - 1).toString();
        sendEmail(lastWeekId);
    }

}
