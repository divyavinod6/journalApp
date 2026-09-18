package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import com.edigest.journalApp.cache.AppCache;
import com.edigest.journalApp.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${weather.api.key}")
    private String apiKey;


//    private static String apiUrl = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
//    private static String apiUrl = "http://api.weatherstack.com/current";

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city){
//        String url = apiUrl.replace("API_KEY",apikey).replace("CITY",city);
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse != null){
            System.out.println("OUTPUT FROM REDIS");
            return weatherResponse;
        }else{
            // IF NOT STORED IN REDIS THEN FETCH API RESPONSE AND STORE IN REDIS FOR LATER
            System.out.println("FETCHING FROM API");
            String url = UriComponentsBuilder.fromHttpUrl(appCache.APPCACHEMAP.get("weather_api"))
                    .queryParam("access_key",apiKey)
                    .queryParam("query",city)
                    .toUriString();

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(url, HttpMethod.GET,null, WeatherResponse.class);
            WeatherResponse resp = response.getBody();
            redisService.set("weather_of_" + city,resp,3000L);
            return resp;

        }

    }

    // TO SEND POST REQUEST TO EXTERNAL API
    /*
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
    */

}
