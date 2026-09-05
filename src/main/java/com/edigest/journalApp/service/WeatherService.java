package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import com.edigest.journalApp.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherService {

    private static String apikey = "fdf9f1383ad287fe13bb5ab01fd43214";
//    private static String apiUrl = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
    private static String apiUrl = "http://api.weatherstack.com/current";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city){
        String url = apiUrl.replace("API_KEY",apikey).replace("CITY",city);
//        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
//                .queryParam("access_key",apikey)
//                .queryParam("CITY",city)
//                .toString();

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(url, HttpMethod.GET,null, WeatherResponse.class);

        return response.getBody();

    }

    // TO SEND POST REQUEST TO EXTERNAL API
    public WeatherResponse getWeatherPost(String city){
//        String url = apiUrl.replace("API_KEY",apikey).replace("CITY",city);
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access_key",apikey)
                .queryParam("CITY",city)
                .toString();

        // TO ADD HEADER
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("key","value");
        // TO ADD REQ BODY
        Users user = Users.builder().username("Vipul").password("vipul").build();
        HttpEntity<Users> httpReq = new HttpEntity<>(user,httpHeaders);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(url, HttpMethod.POST,httpReq, WeatherResponse.class);

        return response.getBody();

    }


}
