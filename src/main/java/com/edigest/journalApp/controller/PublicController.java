package com.edigest.journalApp.controller;

import com.edigest.journalApp.dto.User;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.repository.UsersRepository;
import com.edigest.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Tag(name = "Public APIs")
public class PublicController {


    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        Users users = new Users();
        users.setEmail(user.getEmail());
        users.setPassword(user.getPassword());
        users.setUsername(user.getUsername());
        user.setSentimentalAnalysis(user.isSentimentalAnalysis());
        userService.saveNewUser(users);
    }
}
