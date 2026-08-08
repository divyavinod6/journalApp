package com.edigest.journalApp.controller;


import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public List<Users> getAllUsers(){
        return userService.getAll();
    }

    @GetMapping("/getUser/id/{myId}")
    public Users getUserById(@PathVariable ObjectId myId){
        return userService.getEntryById(myId).orElse(null);
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody Users user){
        userService.saveEntry(user);
    }

    @PutMapping("/updateUser/{username}")
    public ResponseEntity<?> updateUser(@RequestBody Users user,@PathVariable String username){
        Users userDb= userService.findUserByUsername(username);
        if(userDb != null){
            userDb.setUsername(user.getUsername());
            userDb.setPassword(user.getPassword());
            userService.saveEntry(userDb);
            return  new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUser/id/{myId}")
    public String deleteUser(@PathVariable ObjectId myId){
        userService.deleteEntry(myId);
        return "User deleted successfully";
    }
}
