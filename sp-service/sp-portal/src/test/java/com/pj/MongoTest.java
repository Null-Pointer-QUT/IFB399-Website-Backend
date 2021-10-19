package com.pj;

import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.result.UpdateResult;
import com.pj.project4sp.article.Article;
import com.pj.project4sp.article.ArticleRepository;
import com.pj.project4sp.article4comment.CommentParam;
import com.pj.project4sp.article4topic.Topic;
import com.pj.project4sp.user4notify.entity.WsMessage;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@SpringBootTest
public class MongoTest {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private ArticleRepository articleRepo;

    @Test
    public void testListPush() {
        Update update = new Update();
        CommentParam commentParam = new CommentParam();
        commentParam.setComment("here");
        commentParam.setParentId(0L);
//        Article.Comment comment = commentParam.copyToEntity();
//        System.out.println(comment);
//        update.push("comments", comment);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(1431575084410867712L)), update, Article.class);
    }
    @Test
    public void testListInc() {
        Update update = new Update();
        update.inc("comments.nrOfMentioned", 1L);
//        update.pul
        mongoTemplate.updateFirst(Query.query(Criteria.where("comments.commentId").is(1431579883881635840L)), update, Article.class);
    }

    @Test
    public void testCommentId() {
        List<Article> thumbUpArticle = mongoTemplate.find(Query.query(Criteria.where("thumbUp").is(20009)), Article.class);
        thumbUpArticle.forEach(article -> System.out.println(article.getArticleId()));
    }

    @Data
    class Test1C {
        private Long id;
    }

    @Data
    class Test2C {
        private String id;
    }

    @Test
    public void testLong() {
        Test1C test1C = new Test1C();
        test1C.setId(23L);
        Test2C test2C = CglibUtil.copy(test1C, Test2C.class);
        System.out.println(JSONUtil.parseObj(test2C));
    }

    @Test
    public void testFind() {
        Article article = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(1438456031769006080L)), Article.class);
        System.out.println(JSONUtil.parseObj(article));
    }

    @Test
    public void testUpdate() {
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(1L)), Update.update("isRead", Boolean.TRUE), WsMessage.class);
        System.out.println(updateResult);
    }


    @Test
    public void testFind1() {
        LocalDate localDate = LocalDate.parse("2021-08-30");
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        LocalDateTime startOfWeek = localDate.minusDays(dayOfWeek - 1).atStartOfDay();
        LocalDateTime endOfWeek = localDate.plusDays(8 - dayOfWeek).atStartOfDay().minusSeconds(1);

//        Topic topic = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(1438342306462445568L)), Topic.class);
//        List<Article> articles = mongoTemplate.find(Query.query(Criteria.where("_id").in(topic.getRelatedArticles())
//                .and("isPublic").is(Boolean.TRUE)
//                .andOperator(Criteria.where("createTime").gte(startOfWeek),
//                        Criteria.where("createTime").lte(endOfWeek))), Article.class);
//        System.out.println(articles);

//        List<Article> articles2 = mongoTemplate.find(Query.query(Criteria.where("isPublic").is(Boolean.TRUE)
//                .andOperator(Criteria.where("createTime").gte(startOfWeek),
//                        Criteria.where("createTime").lte(endOfWeek))), Article.class);
        List<Article> articles2 = mongoTemplate.find(Query.query(Criteria.where("createTime").lte(LocalDateTime.now(ZoneOffset.ofHours(8)))), Article.class);
        System.out.println(articles2);
    }

    @Test
    public void testDate() {

    }
}
