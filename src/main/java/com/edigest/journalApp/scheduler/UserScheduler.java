package com.edigest.journalApp.scheduler;


import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.enumPack.Sentiment;
import com.edigest.journalApp.model.SentimentData;
import com.edigest.journalApp.repository.UserRepositoryImpl;
import com.edigest.journalApp.service.EmailService;
import com.edigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String,SentimentData> kafkaTemplate;

//    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail(){
        List<Users> users = userRepository.getUserforSentimentAnalysis();

        for(Users user:users){
            List<JourneyEntry> journalList = user.getJourneyEntries();
            List<Sentiment> sentiments = journalList.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment())
                    .toList();

            Map<Sentiment,Integer> sentimentCount = new HashMap<>();
            for(Sentiment sentiment: sentiments){
                if(sentiment != null)  sentimentCount.put(sentiment,sentimentCount.getOrDefault(sentiment, 0) +1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCnt=0;
            for(Map.Entry<Sentiment,Integer> entry: sentimentCount.entrySet()){
                if(entry.getValue() > maxCnt){
                    maxCnt = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if(mostFrequentSentiment != null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days "+ mostFrequentSentiment.toString()).build();
                kafkaTemplate.send("weekly-sentimetns",sentimentData.getEmail(),sentimentData);
                //emailService.sendEmail(user.getEmail() ,"Sentiment Content JournalApp","Hi Happy Birthday Month Pupu!!! Your sentimental analysis from last 7 days is " + mostFrequentSentiment.toString());
            }


        }
    }
}
