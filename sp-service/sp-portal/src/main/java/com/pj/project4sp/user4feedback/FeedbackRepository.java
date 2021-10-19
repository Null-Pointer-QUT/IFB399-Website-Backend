package com.pj.project4sp.user4feedback;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface FeedbackRepository extends MongoRepository<Feedback, Long> {

}
