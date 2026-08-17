package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.repository.UsersRepository;
import com.edigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {


    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody Users user){
        userService.saveNewUser(user);
    }
}
