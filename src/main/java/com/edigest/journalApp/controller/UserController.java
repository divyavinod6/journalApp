package com.edigest.journalApp.controller;


import com.edigest.journalApp.api.response.WeatherResponse;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.service.UserService;
import com.edigest.journalApp.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;


    @GetMapping("/getAllUsers")
    public List<Users> getAllUsers(){
        return userService.getAll();
    }

    @GetMapping("/getUser/id/{myId}")
    public Users getUserById(@PathVariable ObjectId myId){
        return userService.getEntryById(myId).orElse(null);
    }

    /*
    // ADDED TO PUBLIC CONTROLLER TO BYPASS AUTHENTICATION FROM SPRING SECURITY
    @PostMapping("/addUser")
    public void addUser(@RequestBody Users user){
//        userService.saveEntry(user);
        userService.saveNewUser(user); // for hashed pwd store in DB
    }
     */


    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody Users user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users userDb= userService.findUserByUsername(username);
        // updating username and pwd given by user
        userDb.setUsername(user.getUsername());
        userDb.setPassword(user.getPassword());
        userService.saveNewUser(userDb);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // only delete user after its authenticated
        String username = authentication.getName();
        userService.deleteByUsername(username);
        return "User deleted successfully";
    }

    @GetMapping("/getGreetings")
    public ResponseEntity<?> getGreetings(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username  =auth.getName();
        WeatherResponse weather = weatherService.getWeather("MUMBAI");
        if(weather!= null){
            return new ResponseEntity<>("Hi! "+ username + " weather is "  + weather.getCurrent().getTemperature() + "`C",HttpStatus.OK);
        }
        return new ResponseEntity<>("Hi! "+ username,HttpStatus.OK);


    }
}
