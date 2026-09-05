package com.edigest.journalApp.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class QuoteResponse {

    public String quote;
    public String author;
    public String work;
    public ArrayList<String> categories;
}



