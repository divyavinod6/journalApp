package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.service.JournalEntryService;
import com.edigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JourneyEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping("/addEntry")
    public ResponseEntity<JourneyEntry> createEntry(@RequestBody JourneyEntry j){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username  =  authentication.getName();

            System.out.println("username :" + username);
            journalEntryService.saveEntry(j,username);
            return new ResponseEntity<>(j,HttpStatus.CREATED);

        }catch(Exception e){
            return new ResponseEntity<>(j, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getAllEntries")
    public ResponseEntity<List<JourneyEntry>> getAllJournalEntryOfUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findUserByUsername(username);
        try{
//            List<JourneyEntry> j = journalEntryService.getAll();
            List<JourneyEntry> j = user.getJourneyEntries();
            return  new ResponseEntity<>(j,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    // PathVariable is when input is along api url eg journal/getEntry/id/2
    // Request Parameter is when input is with ? in url/Params eg journal/getEntry/id?id=2
    @GetMapping("/getEntry/id/{myId}")
    public ResponseEntity<JourneyEntry> getEntry(@PathVariable ObjectId myId){
        try{
            Optional<JourneyEntry> obj = journalEntryService.getEntryById(myId);
            if(obj.isPresent()){
                return new ResponseEntity<>(obj.get(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/editEntry/{username}/{myId}")
    public ResponseEntity<JourneyEntry> updateJournalEntry(@PathVariable ObjectId myId,String username,@RequestBody JourneyEntry req){
        try{
            journalEntryService.updateEntry(myId,username,req);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/deleteEntry/{username}/{myId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId myId,@PathVariable String username){
        try {
            journalEntryService.deleteEntry(myId,username);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
