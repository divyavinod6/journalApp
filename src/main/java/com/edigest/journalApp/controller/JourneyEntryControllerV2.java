package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JourneyEntryControllerV2 {

    @Autowired
    JournalEntryService journalEntryService;

    Map<String, JourneyEntry> journalEntries = new HashMap<String, JourneyEntry>();

    @PostMapping("/addEntry")
    public JourneyEntry createEntry(@RequestBody JourneyEntry j){
        j.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(j);
        return j;
    }

    @GetMapping("/getAllEntries")
    public List<JourneyEntry> finAllEntry(){

        return  journalEntryService.getAll();
    }

    // PathVariable is when input is along api url eg journal/getEntry/id/2
    // Request Parameter is when input is with ? in url/Params eg journal/getEntry/id?id=2
    @GetMapping("/getEntry/id/{myId}")
    public JourneyEntry getEntry(@PathVariable ObjectId myId){
        return journalEntryService.getEntryById(myId).orElse(null);
    }



    @PutMapping("/editEntry/{myId}")
    public void updateJournalEntry(@PathVariable ObjectId myId,@RequestBody JourneyEntry req){
        journalEntryService.updateEntry(myId,req);

    }

    @DeleteMapping("/deleteEntry/{myId}")
    public String deleteEntry(@PathVariable ObjectId myId){
        journalEntryService.deleteEntry(myId);
        return "Deleted Successfully";
    }

}
