package com.pj.project4sp.user4notify;

import com.pj.project4sp.user4notify.entity.WsMessage;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface WsMessageRepository extends MongoRepository<WsMessage, Long> {

}
