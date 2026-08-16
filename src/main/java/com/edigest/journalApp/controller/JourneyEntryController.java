package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.JourneyEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
//@RequestMapping("/journal")
public class JourneyEntryController {

//    Map<String, JourneyEntry> journalEntries = new HashMap<String, JourneyEntry>();
//
//    @GetMapping("/getAllEntries")
//    public List<JourneyEntry> getJournalEntry(){
//        return  new ArrayList<>(journalEntries.values());
//    }
//
//    @GetMapping("/getEntry/id/{myId}")
//    public JourneyEntry getEntry(@PathVariable long myId){
//        return journalEntries.getOrDefault(myId, null);
//    }
//
//    @PostMapping("/addEntry")
//    public String addJournalEntry(@RequestBody JourneyEntry j){
//        JourneyEntry put = journalEntries.put(j.getId(), j);
//        return "Entry inserted successfully";
//    }
//
//    @PutMapping("/editEntry/{myId}")
//    public JourneyEntry updateJournalEntry(@PathVariable String myId,@RequestBody JourneyEntry req){
//        if(journalEntries.containsKey(myId)){
//            return journalEntries.put(myId,req);
//            return "updation Successfull";
//        }else{
//            return null;
//        }
//
//    }

    //   PathVariable is when input is along api url eg journal/getEntry/id/2
    //   Request Parameter is when input is with ? in url/Params eg journal/getEntry/id?id=2

    // When an unannotated parameter like Long id is present in a controller method, Spring MVC treats it by default as a Request Parameter (@RequestParam)
    // 1) If you want to read raw text or JSON from the body, you must use the @RequestBody annotation:
    // 2) For DELETE operations, standard REST API practice passes the resource identifier directly in the URL: (@DeleteMapping("/deleteEntry/{id}")
    //                                                                                                       public String deleteEntry(@PathVariable Long id) {...}
    // 3) If you keep your method exactly as written, you don't need to change the Java code—just change how you make the API call in Postman/Curl by passing id in the URL params:
        //URL: DELETE localhost:8080/journal/deleteEntry?id=1
        //Body: Empty
    //Spring looked for a URL parameter named id (e.g., localhost:8080/journal/deleteEntry?id=1).
//    @DeleteMapping("/deleteEntry")
//    public String deleteEntry(Long id){
//        if(journalEntries.containsKey(id)){
//            journalEntries.remove(id);
//        }else{
//            return "ID not found in Entries";
//        }
//        return "Deleted Successfully";
//    }


//    @DeleteMapping("/deleteEntry/{myId}")
//    public String deleteEntry(@PathVariable Long myId){
//        if(journalEntries.containsKey(myId)){
//            journalEntries.remove(myId);
//        }else{
//            return "ID not found in Entries";
//        }
//        return "Deleted Successfully";
//    }

}
