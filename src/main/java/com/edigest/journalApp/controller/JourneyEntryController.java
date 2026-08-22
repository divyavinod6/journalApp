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
import java.util.stream.Collectors;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); //  we get user trying to view myID journal entry
        // we fetch all journal entries corresponding this username and filter in it to find journal entry with myId
        List<JourneyEntry> journeyEntryList = userService.findUserByUsername(username).getJourneyEntries().stream()
                .filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!journeyEntryList.isEmpty()){
            Optional<JourneyEntry> obj = journalEntryService.getEntryById(myId);
            if(obj.isPresent()){
                return new ResponseEntity<>(obj.get(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 authenticated not authorised


    }

    @PutMapping("/editEntry/{myId}")
    public ResponseEntity<JourneyEntry> updateJournalEntry(@PathVariable ObjectId myId,@RequestBody JourneyEntry req){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            // to check if this user has corresponding myId journal entry or not
            List<JourneyEntry> journeyEntryList = userService.findUserByUsername(username).getJourneyEntries().stream()
                            .filter(x -> x.getId().equals(myId)).toList();

            // if myID journal entry is present in User of username then they can edit it
            if(!journeyEntryList.isEmpty()){
                journalEntryService.updateEntry(myId,username,req);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/deleteEntry/{myId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId myId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<JourneyEntry> entryList = userService.findUserByUsername(username).getJourneyEntries().stream()
                .filter(x -> x.getId().equals(myId)).toList();

        if(!entryList.isEmpty()){
            try {
                boolean ifRemoved = journalEntryService.deleteEntry(myId,username);
                if(ifRemoved){
                    return new ResponseEntity<>(HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

            }catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 authenticated not authorised
        }

    }

}
