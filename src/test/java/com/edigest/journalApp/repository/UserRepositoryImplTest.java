package com.edigest.journalApp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mongodb.assertions.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testGetUserforSentimentAnalysis(){
        assertNotNull(userRepositoryImpl.getUserforSentimentAnalysis());

    }
}
