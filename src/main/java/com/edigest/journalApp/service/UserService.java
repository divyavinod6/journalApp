package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JourneyEntry;
import com.edigest.journalApp.entity.Users;

import com.edigest.journalApp.repository.JournalEntryRepository;
import com.edigest.journalApp.repository.UsersRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    // BUSINESS LOGIC

    @Autowired
    UsersRepository userRepository;

    public void saveEntry(Users journeyEntry){
        userRepository.save(journeyEntry);
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
}