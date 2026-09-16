package com.edigest.journalApp.scheduler;

import com.edigest.journalApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTest {

    @Autowired
    private UserScheduler userScheduler;

    @Test
    public void testFetchUsersAndSendMail(){
        userScheduler.fetchUsersAndSendMail();
    }
}
