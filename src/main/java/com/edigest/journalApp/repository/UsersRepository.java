package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users,String> {
    Users findByUsername(String username);

}

