package com.edigest.journalApp.service;

import com.edigest.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserScheduler userScheduler;

    @Test
    @Disabled
    public void testSendEmail(){
        emailService.sendEmail("thisisdivyavinod@gmail.com","JournalApp", "Hi this is JournalApp");
    }

    @Test
    public void testfetchUsersAndSendMail(){
        userScheduler.fetchUsersAndSendMail();
    }
}
