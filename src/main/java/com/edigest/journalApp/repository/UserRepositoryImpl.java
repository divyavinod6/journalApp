package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Users> getUserforSentimentAnalysis(){
        Query query= new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(
                Criteria.where("sentimentalAnalysis").is(true),
                Criteria.where("email").exists(true).ne("")// ne = not equal
        ));

        List<Users> users = mongoTemplate.find(query, Users.class);
        return users;
    }
}
