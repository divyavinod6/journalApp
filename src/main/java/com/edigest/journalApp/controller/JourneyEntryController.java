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
//    // PathVariable is when input is along api url eg journal/getEntry/id/2
//    // Request Parameter is when input is with ? in url/Params eg journal/getEntry/id?id=2
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
////            return "updation Successfull";
//        }else{
//            return null;
//        }
//
//    }

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

    // BEST PRACTICES
    // CONTROLLER - > SERVICE -> REPOSITORY
    // make your repo extend MongoRepository , it is a standard CRUD opn repo given by Spring Data mongoDb
        // eg extends MongoRepository<BEan name, id type>
        // this bean would be our collection / table name -> add @Document on bean class to enable it and @Id of unique variable
        // if collection/table name is different than bean class name , @Document(collection = "table_name")
    // inject this repo in service and use save method to add entry
    // created Date variable too and changed datatype of variable to ObjectId so that we can parse it to findById method in Service

    // RESPONSE ENTITYT : HTTP CODES
    // HTTP code are 3 digit numerical code returned by Web server as part of the response to an HTTP request
    // status codes are used to convey  information about resutls/requested operation
    // grouped into 5 categories based on there first digit:
        // 1xx Informational : indicate req was recieved and is being processed by server , used for info purposes
        // 2xx Successfull : indicate req was recieved , and processed successfully
            // 200 OK :processed and is returning req resource
            // 201 Created: processed and returning new resource which was requested
            // 204 No Content : req is successfull + no response body (used for operation that dont return data eg. deletion)
        // 3xx Redirection : indicates that further action is required to complete the request . Used when client need to take additional steps to access req resource
            // 301 Moved Permanently: req resource has permanently moved to a different url
            // 302 Found : req resource has temporarily moved to a different url. When the server sends 302 it sends it includes location header field which has temp url client must redirect to
            // 304 Not modified: if client cached resource of requested resource is still valid , so server sends 304 to indicate that client can use its cached copy
        // 4xx Client Error : indicates error on clients part eq, malformed request, authentication issues
            // 400 Bad Req: server cant understand/ process client req because of invalid syntax or other client issue
            // 401 Unauthorised: client need to provide authentication credentails to access requested resource
            // 403 Forbidden: client is authenticated but doesnt have the permission to access req resource
        // 5xx Server Error: indicates error on server's part while trying to fulfill the req
            // 500 Internal Server Error : generic message indicating something wrong on the server and so it couldnt handle the request
            // 502 Bad Gateway : server acting as gateway/proxy received invalid response from upstream server
            // 503 Service unavailble : server currenlty not able to handle req due to temporary overloading/ maintainance

    // Response Entity : this class in part of Spring Framework and is commonly used to customize HTTP response
        // it provides methods for setting response status, headers and body
        // u can use it to return diff types of data (JSON,XML,HTML)
        // u can use generics to specify type of data u are returning


    // Lombok : java library, aims to reduce boilerplate code eg getter,setter

    // User Login : 1) created User collection in Mongodb which'll store username, pwd and entries belonging to that user
        // authentication
            // created User bean with variable constraints like NotNull, Indexed (for fasted search, have to include auto-index-creation in application properties to enable indexing)
            // TO LINK user(having journal entries) and journal_entries(table) we use @DBref to List<journalEntries> so that i can store reference of all journal entries in collection
            // NOTE : put NO ARGS CONSTRUCTOR IN JOURNEY BEAN FOR DESERIALISATION (JSON TO POJO)
            // CASCADE DELETE : now users and journal Entry is linked such as every user has list of journal entries and it that list ID of jounral entry
                            //when this journal entry is deleted , we also have to delete it from its users list. This is cascade delete which happend automatically  in RDBMS but not in Mongodb
                            // in Mongodb we have to do it manually. But when u run spring, trying to save new journal entry in user which already has non existent journal entry(coz u deleted it) , SPRING will make users journalEntry list consistent (will delete old non existent journal entry delete)
                            // spring will make consistent in users next save entry but this wont be replicated on mongodb
        // @TRANSACTIONAL helps achieve atomacity and isolation
            // u put that on methods so spring identifies it as one unit of execution
            // then add @EnableTransactionManagement to create Transaction context for every method call
            // PlatformTransactionManager is the interface which manages(successfull then commit, failure then rollback) all transaction content( Ram hit api and Shyam hit api, they'll both have different transaction context)
            // so this is completed in MongoTransactionManager(takes instance of MongoDB connection : given by MongoDatabaseFactory)
            // NOTE: TRANSACTION is only allowed on mongodb on Replica set (we have only one instance in mongodb, so we create a replica instance/sharded cluster: adding rs0 in mongodb config file)



}
