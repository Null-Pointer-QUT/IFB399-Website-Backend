package com.pj.project4sp.user4feedback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService{

    private final FeedbackRepository feedbackRepo;

    private final MongoTemplate mongoTemplate;

    @Override
    public void upload(FeedbackUploadParam param) {
        Feedback feedback = param.copyToEntity();
        feedbackRepo.insert(feedback);
    }
}
