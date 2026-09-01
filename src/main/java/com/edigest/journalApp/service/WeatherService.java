package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    private static String apikey = "fdf9f1383ad287fe13bb5ab01fd43214";
    private static String apiUrl = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city){
        String url = apiUrl.replace("API_KEY",apikey).replace("CITY",city);

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(url, HttpMethod.GET,null, WeatherResponse.class);

        return response.getBody();

    }


}
