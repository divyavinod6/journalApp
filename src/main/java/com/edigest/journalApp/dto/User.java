package com.edigest.journalApp.dto;

import com.edigest.journalApp.entity.JourneyEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class User {

    private String username;
    private String password;
    private String email;
    private boolean sentimentalAnalysis;
//    private List<JourneyEntry> journeyEntries = new ArrayList<>();
//    private List<String> roles; // for authentication
}
