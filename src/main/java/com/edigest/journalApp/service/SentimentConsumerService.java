package com.edigest.journalApp.service;

import com.edigest.journalApp.enumPack.Sentiment;
import com.edigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    public void consumer(SentimentData sentimentData){
        sendEmail(sentimentData);
    }


    public void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
    }
}
