package com.edigest.journalApp.cache;

import com.edigest.journalApp.entity.ConfigJournalAppEntity;
import com.edigest.journalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;
    
    public Map<String,String> APPCACHEMAP;

    @PostConstruct
    public void init(){
        APPCACHEMAP = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity entity: all){
            APPCACHEMAP.put(entity.getKey(),entity.getValue());
        }
    }
}
