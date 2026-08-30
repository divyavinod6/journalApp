package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;

import com.edigest.journalApp.repository.JournalEntryRepository;
import com.edigest.journalApp.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    // BUSINESS LOGIC

    @Autowired
    UsersRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveEntry(Users user){
        userRepository.save(user);
    }

    // new method to implement passwordEncoder for SpringSecurity
    public boolean saveNewUser(Users user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return  true;
        }catch (Exception e){

            log.trace("TRACE {}", user.getUsername());
            log.debug("DEBUG {}", user.getUsername());

            log.info("INFO", e);
            log.warn("WARN", e);
            log.error("ERROR", e);
            return false;
        }

    }
    public List<Users> getAll(){
        return userRepository.findAll();
    }

    public Optional<Users> getEntryById(ObjectId id){
        return userRepository.findById(String.valueOf(id));
    }

    public void deleteEntry(ObjectId id){
        userRepository.deleteById(id.toString());
    }

    public Users findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void deleteByUsername(String username){
        userRepository.deleteAllByUsername(username);
    }

    public void saveAdminUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ADMIN","USER"));
        userRepository.save(user);
    }
}