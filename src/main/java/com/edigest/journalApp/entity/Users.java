package com.edigest.journalApp.entity;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class Users {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;

    @DBRef
    private List<JourneyEntry> journeyEntries = new ArrayList<>();

    private List<String> roles; // for authentication

    public Users(@Nonnull String username, @Nonnull String password){
        this.username = username;
        this.password = password;
        this.journeyEntries = new ArrayList<>();
    }
}
