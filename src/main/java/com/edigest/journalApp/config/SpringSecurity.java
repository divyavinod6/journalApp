package com.edigest.journalApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.PasswordView;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class SpringSecurity{

    @Autowired
    private UserDetailsService userDetailsService; // no need as this is automatically autowired

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(
                request -> request
                        .requestMatchers("/public/**").permitAll() // Public endpoints
                        .requestMatchers("/journal/**","/user/**").authenticated() // Secure endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Secure endpoints
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()) // Enables HTTP Basic Auth (for Postman testing)
                .csrf(AbstractHttpConfigurer::disable) // // Disable CSRF for REST APIs
                .build();
        // what teacher coded
        /*
        http.authoriseRequest().antMatchers("/journal/**").authenticated().anyRequest().permitAll().and().httpBasic();
         */
    }
    /*
    // No need this method in Spring Boot 3 as UserDetailsService and PasswordEncoder are registered as Spring @Beans, Spring Security automatically wires them together behind the scenes.
    protected void configure(AuthenticationManagerBuilder auth) throws  Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}
