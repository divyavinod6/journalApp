package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.QuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class QuotesService {

    private static String apikey = "8rjYI8LfM0WwYv0P28bGgWGoAaiUF6QbBfnMRyjU";
    private static String apiUrl = "https://api.api-ninjas.com/v2/quotes?categories=success%2Cwisdom";

    @Autowired
    private RestTemplate restTemplate;

    public QuoteResponse getQuoteMethod(){
        try{
            // build url using UriComponentsBuilder
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl).toUriString();
            // set Headers using HttpHeaders
            HttpHeaders header = new HttpHeaders();
            header.set("X-Api-Key",apikey);
            // wrap header in Http Entity
            HttpEntity<String> entity = new HttpEntity<>(header);
            // making api call using response entity
            ResponseEntity<QuoteResponse[]> result = restTemplate.exchange(url, HttpMethod.GET, entity, QuoteResponse[].class);
            System.out.println("quore resutl : " + result);
            QuoteResponse[] resArr = result.getBody();
//        assert result.getBody() != null;
            if(resArr != null && resArr.length!= 0){
                return resArr[0];
            }else return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
