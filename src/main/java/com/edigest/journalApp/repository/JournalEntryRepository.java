package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.JourneyEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface JournalEntryRepository extends MongoRepository<JourneyEntry,String> {

}
