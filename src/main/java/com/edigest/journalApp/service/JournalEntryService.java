package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    // BUSINESS LOGIC

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JourneyEntry journeyEntry, String username){
        Users user = userService.findUserByUsername(username);

        journeyEntry.setDate(LocalDateTime.now());
        JourneyEntry saved = journalEntryRepository.save(journeyEntry);
        user.getJourneyEntries().add(saved);
        userService.saveEntry(user);
    }

    public List<JourneyEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JourneyEntry> getEntryById(ObjectId id){
        return journalEntryRepository.findById(String.valueOf(id));
    }

//    public void updateEntry(ObjectId myId,JourneyEntry newObj){
//        JourneyEntry old  = journalEntryRepository.findById(myId.toString()).orElse(null);
//        if(old != null){
//
//            old.setContent(newObj.getContent() != null && !newObj.getContent().isEmpty() ? newObj.getContent() : old.getContent());
//            old.setTitle(newObj.getTitle() != null && !newObj.getTitle().isEmpty() ? newObj.getTitle() : old.getTitle());
//            old.setDate(LocalDateTime.now());
//
//            journalEntryRepository.save(old);
//        }else{
//            System.out.println("no match found for this operation");
//        }
//
//    }

    public void deleteEntry(ObjectId id){
        journalEntryRepository.deleteById(id.toString());
    }
}