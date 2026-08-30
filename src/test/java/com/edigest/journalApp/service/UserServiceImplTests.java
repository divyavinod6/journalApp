package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.Users;
import com.edigest.journalApp.repository.UsersRepository;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsernameTest(){
        when(usersRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Users.builder().username("ram").password("ksrnfr").roles(new ArrayList<>()).build());
        UserDetails user = userDetailsService.loadUserByUsername("ram");

        Assertions.assertNotNull(user);
    }
}
