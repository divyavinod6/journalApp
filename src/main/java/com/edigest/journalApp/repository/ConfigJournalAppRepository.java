package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.ConfigJournalAppEntity;
import com.edigest.journalApp.entity.JourneyEntry;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity,String> {

}
