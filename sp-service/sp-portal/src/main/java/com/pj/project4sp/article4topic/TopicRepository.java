package com.pj.project4sp.article4topic;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface TopicRepository extends MongoRepository<Topic, Long> {

    Optional<Topic> findTopicByTopicName(String topicName);
}
