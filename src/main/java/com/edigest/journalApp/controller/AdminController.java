package com.edigest.journalApp.controller;


import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsersForAdmin(){
        try{

            List<Users> listAllUser  = userService.getAll();
            if(listAllUser!= null && !listAllUser.isEmpty()){
                return new ResponseEntity<>(listAllUser,HttpStatus.OK);
            }else return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createUserAdmin(@RequestBody Users user){
        userService.saveAdminUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
