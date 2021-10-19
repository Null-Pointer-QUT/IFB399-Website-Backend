package com.pj.project4sp.article4digest;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface DigestRepository extends MongoRepository<Digest, Long> {

}
