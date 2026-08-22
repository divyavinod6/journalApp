package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void saveEntry(JourneyEntry journeyEntry, String username){
        Users user = userService.findUserByUsername(username);
        if(user == null) user = new Users(username,"default");
        System.out.println("user : " + user);
        journeyEntry.setDate(LocalDateTime.now());
        JourneyEntry saved = journalEntryRepository.save(journeyEntry);
        user.getJourneyEntries().add(saved); // IF ERROR HERE IT SHOULD ROLLBACK JOURNAL INSERT IN JOURNAL COLLECTION
        System.out.println("user : " + user);
        userService.saveEntry(user);
    }

    public List<JourneyEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JourneyEntry> getEntryById(ObjectId id){
        return journalEntryRepository.findById(String.valueOf(id));
    }

    public void updateEntry(ObjectId myId,String username,JourneyEntry newObj){
//        Users user = userService.findUserByUsername(username);
//        if(user == null) return;
//        List<JourneyEntry> userList = user.getJourneyEntries();
        JourneyEntry old  = journalEntryRepository.findById(myId.toString()).orElse(null);
        if(old != null){

            old.setContent(newObj.getContent() != null && !newObj.getContent().isEmpty() ? newObj.getContent() : old.getContent());
            old.setTitle(newObj.getTitle() != null && !newObj.getTitle().isEmpty() ? newObj.getTitle() : old.getTitle());
            old.setDate(LocalDateTime.now());

            journalEntryRepository.save(old);
        }else{
            System.out.println("no match found for this operation");
        }

    }

    @Transactional
    public boolean deleteEntry(ObjectId id,String username){
        boolean removed = false;
        try {
            Users user = userService.findUserByUsername(username);
            if(username == null) return removed;
            removed = user.getJourneyEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id.toString());
            }
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("ERROR OCCURED WHILE DELETING JOURNAL ENTRT");
        }
        return removed;


    }
}