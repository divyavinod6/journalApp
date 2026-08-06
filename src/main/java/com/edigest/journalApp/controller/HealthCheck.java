package com.edigest.journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    // ORM is a technique used to map java objects to DB tables
    // JPA: Java persistance api: persistance means storing permanently , api is set of rules
        // so JPA is set of rules to achieve ORM
        // only used with Relational DB (predefined schema)
        // Persistance providers/ORM tools : specific implementation of the JPA specification eg: hibernate.EclipseLink and OpenJPA
            // so JPA is an interface and Hibernate is its implementaion
    // Spring JPA: build on top of JPA
            //   : not JPA implementation itself, it simplifies JPA by providing abstraction and utilities
            // u still need JPA tool for this
            // eg there's no JPA for MongoDB(NoSQl,flexible schema) so Spring Data mongodb serves as persistance provider for mongodb
            // 2 ways to interact with DB when using Spring Data JPA (sql) and Spring Mongodb(MongoDB) are: QUERY METHOD DSL and CRITERIA API
            // (Spring Data JPA: part of spring framework, simplifies data access in java application)
}
