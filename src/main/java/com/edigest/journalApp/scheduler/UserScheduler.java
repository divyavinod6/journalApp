package com.edigest.journalApp.scheduler;


import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.repository.UserRepositoryImpl;
import com.edigest.journalApp.service.EmailService;
import com.edigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@EnableScheduling
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail(){
        List<Users> users = userRepository.getUserforSentimentAnalysis();

        for(Users user:users){
            List<JourneyEntry> journalList = user.getJourneyEntries();
            List<String> filteredList = journalList.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getContent())
                    .toList();
            String content = String.join("",filteredList);
            String sentiment = sentimentAnalysisService.getSentiment(content);
            //emailService.sendEmail(user.getEmail() ,"Senti content JournalApp","Hi this is sentimental analysis from last 7 days");

        }
    }
}
