package com.pj.project4sp.article4topic;

import java.util.List;

public interface ITopicService {

    void collectTopic();

    Boolean changeIsPublic(Long topicId);

    List<Topic> getAllTopics();

    void changeTopicDetails(TopicUpdateParam param);

    void changeTopicListDetail(TopicUpdateListParam param);

    List<TopicIntroVo> getPublicTopics();

    Boolean subscribe(Long topicId);
}
