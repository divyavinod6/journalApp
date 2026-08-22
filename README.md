# Spring Boot Journal Application

A RESTful web application built with Spring Boot and MongoDB for managing journal entries and user accounts. This repository tracks the core concepts, best practices, and architecture patterns implemented throughout development.

---

## Architecture & Best Practices

The application follows the standard three-tier architecture:

```text
CONTROLLER ──► SERVICE ──► REPOSITORY ──► MONGODB
```

### Controller Layer: Exposes REST API endpoints and manages HTTP request/response payloads.

### Service Layer: Houses core business logic, validation rules, and transaction boundaries.

### Repository Layer: Extends MongoRepository<Entity, IdType> to provide out-of-the-box CRUD operations powered by Spring Data MongoDB.


### ORM is a technique used to map java objects to DB tables
JPA: Java persistance api: persistance means storing permanently , api is set of rules
so JPA is set of rules to achieve ORM
only used with Relational DB (predefined schema)

Persistance providers/ORM tools : specific implementation of the JPA specification eg: hibernate.EclipseLink and OpenJPA
so JPA is an interface and Hibernate is its implementaion

Spring JPA: build on top of JPA
: not JPA implementation itself, it simplifies JPA by providing abstraction and utilities
u still need JPA tool for this
eg there's no JPA for MongoDB(NoSQl,flexible schema) so Spring Data mongodb serves as persistance provider for mongodb
2 ways to interact with DB when using Spring Data JPA (sql) and Spring Mongodb(MongoDB) are: QUERY METHOD DSL and CRITERIA API
(Spring Data JPA: part of spring framework, simplifies data access in java application)

### 🛠️ Key Spring MVC & Controller Concepts

## Request Inputs

    1.1 @PathVariable: Used when parameters are embedded directly in the URL path.

    Example: /journal/getEntry/id/2

    1.2 @RequestParam: Used when parameters are passed as URL query arguments (?key=value).

    Example: /journal/getEntry/id?id=2

    --> Default Behavior: Unannotated method arguments (e.g., Long id) are implicitly treated as @RequestParam by Spring MVC.

    1.3 @RequestBody: Maps incoming raw JSON payloads to Java POJOs.

2. RESTful Endpoint Design
   Resource identifiers for DELETE operations are passed directly in the URL path (@PathVariable):

   Eg . Java
   @DeleteMapping("/deleteEntry/{id}")
   public String deleteEntry(@PathVariable ObjectId id) { ... }

## 🌐 HTTP Status Codes & ResponseEntity

ResponseEntity is used across controllers to explicitly control the HTTP status code, headers, and body returned to the client.

Category Overview
1xx Informational: Request received and being processed.

2xx Success:

200 OK: Request succeeded and returned data. (eg GET)

201 Created: Resource successfully created. (e.g., POST).

204 No Content: Action succeeded with no body returned (e.g., successful deletion).

3xx Redirection:

301 Moved Permanently: Resource relocated permanently.

302 Found: Resource temporarily moved.

304 Not Modified: Cached client copy is still valid.

4xx Client Error:

400 Bad Request: Malformed payload or invalid client syntax.

401 Unauthorized: Missing or invalid AUTHENTICATION credentials.

403 Forbidden: Authenticated, but lacks required permissions. (Not AUTHORISATION)

5xx Server Error:

500 Internal Server Error: Unhandled exception on the server side.

502 Bad Gateway: Invalid response from an upstream server.

503 Service Unavailable: Server is temporarily overloaded or undergoing maintenance.

## 🗄️ Database Mapping & Relationships (MongoDB)

Data Annotations
@Document(collection = "table_name"): Maps a Java domain class to a MongoDB collection.

@Id: Marks a property as the document's primary key (ObjectId).

@Indexed(unique = true): Creates database-level indexes for fast lookups (requires spring.data.mongodb.auto-index-creation=true in configuration).

Linking Users & Journal Entries
@DBRef: Used inside the Users entity (List<JourneyEntry>) to store MongoDB DBRef references linking entries to specific users.

Cascade Deletes: MongoDB does not automatically perform cascade deletions. When a journal entry is deleted, references are removed manually in application code. Spring Data updates user reference arrays on subsequent saves to maintain object consistency.

Jackson Deserialization: Domain entities require no-argument constructors (@NoArgsConstructor / default constructor) to support JSON-to-POJO mapping. {Added in Journey Bean}

## 🔄 Transactions & Database Replication

Multi-document operations (e.g., creating a journal entry and adding its reference to a user document simultaneously) require ACID guarantees:

1. @Transactional: Applied to service methods to designate single units of work.(if error occurs in btw execution , everything is rollbacked by PlatformTransactionManager)

2. @EnableTransactionManagement: Enables Spring transaction management proxies.( added in @SpringBootApplication class)

3. @Bean for PlatformTransactionManager(interface) . We got MongoTransactionManager instance in our method : Configured as a PlatformTransactionManager bean accepting a MongoDatabaseFactory to handle commit/rollback lifecycles across isolated contexts.

Note : Replica Set Requirement: MongoDB ACID transactions require a replica set or sharded cluster.

Local Environment: Configured as a single-node replica set (replSetName: "rs0" in mongod.cfg).

Cloud Environment: Hosted on MongoDB Atlas, which provides automatic multi-node replica sets out of the box.

⚡ Additional Libraries
Lombok: Reduces Java boilerplate code (e.g., @Data, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor).

### Spring Security
Security Framework used in spring to handle authentication and authorisation.

Authentication : process for verifying users identity (username and pwd).

Authorisation : process of managing access to resources/actions bases on users role and permissions.

Once this dependency is added Spring Auto Configuration will apply security features to all endpoints

SpringSecurity uses HTTP basic authentication (client sends Auth header <encoded String>. Servre decodes string extracts username pwd and verifies. If correct, access grant or send 403 Unauthorised)
If user is not created , Spring automatically creates default user with random pwd thats printed on console log while startup.
(U can configure user and pwd in application.prop or DB)
So now when u hit any api in controller, u have to add Auth->Basic Auth-> username: user,pwd->paste pwd from console
(this is automatically added in Header->Auth : base64 encrypted of username:password)

Customise Spring Security: u would only need this on some endpoints that too specific pwd on username
1) create a config class, add @Configuration,@EnableWebSecurity and extends WebSecurityConfigAdapter(utility class in Spring Security frameword to customise Spring Sec)
2) since this is depricated now, No Class Inheritance: No need to extend WebSecurityConfigurerAdapter.

SecurityFilterChain Bean: Register a @Bean that takes HttpSecurity as a parameter and returns http.build().

Lambda DSL & requestMatchers: Use .authorizeHttpRequests() with .requestMatchers(...) instead of the old antMatchers(...).

CSRF Handling: Turn off CSRF for non-browser stateless REST APIs using .csrf(AbstractHttpConfigurer::disable).


Spring Security also provides default Login and Logout funtionality.

NOTE: BASIC AUTHENICATION BY DESIGN IS STATELESS(no history, 2nd request doesnt know anything about 1st: i entered username and pwd and got authenticated and got response, then when i try to send another req, again same proccess of auth has to be done)
HTTP is stateless but still Spring Security manages authentication as mixed Basic Auth. It uses SESSION MANAGEMENT, this is not standard behaviour and requires additional setup and logic. Here, once user credentials are verified via Basic Auth, session is established and client is given session cookie. This way client dont have to send authorisation header with every request and server can rely on session cookie to identify auth use.
STEPS
1) Session Creation: after successful authentication, HTTP session is formed (auth details stored in it)
2) Session cookie: JSESSIONID cookie(in HEADERS) is sent to client/brower, which is sent back with requests(GET,POST etc) helping the server recognise user session
3) Security Context: using JSESSIONID Spring Sec fetched auth details for each req
4) Session Timeout : Session have limited life, u are inactive for particular time u get logged out
5) Logout : when loggin out, session ends and related cookie is removed
6) Remember Me: Spring Sec can remember you even after session ends using different persistent cookie(longer lifespan)

The password given by Spring in console is Admin pwd as it can be used for all users. We want to authenticate users based on there credentials stored in MongoDB
So our users and there pwd(hashed) are stored in MongoDB, so when trying to login system should fetch pwd from db to validate

#### IMPLEMENTION FOR AUTHORISATION
1) create User entity : we already have that just add List of roles to store which role is given to user
2) create Repo to interact with DB: already have that too
3) UserDetailsService implementaion to fetch user details: (DOUBLE SHIFT CLICK enter UserDetailsService) its and interface which will help us find user in DB. So we create UserDetailsServiceImpl to implement this interface.
   it has one method loadUserByUsername which returns UserDetails(bean given by this interface) thats why (   org.springframework.security.core.userdetails)User=UserDetails
4) Create Security config class to integrate everything  : we created SpringConfig and injected UserDetailServiceImpl in it
                  : create password encoder which returns new BCryptPasswordEncoder()
NOTE: Legacy configure(AuthenticationManagerBuilder auth) Method: Overriding configure(...) belonged to WebSecurityConfigurerAdapter. In Spring Boot 3+, Spring Security handles UserDetailsService and PasswordEncoder wiring automatically via Dependency Injection.
NOTE: CSRF: Cross Site Request Forgery: when enabled Spring Sec expects u to send Token in CSRF req. Since we are creating a Stateless api , we wil disable CSRF
DEMO : 
- Now we create updated User controller methods : user creation method saveNewUser(which will convert incoming pwd to base64 and then save to dB) is moved to Public Controller so we can create user without auth
- update PUT mapping not to input username from url but from SECURITY CONTEXT HOLDER, by passing username ,pwd from bearer auth. This pwd then is converted to hash and compared with hash from db in method configure (in-built method in Spring Sec)
- updated DELETE user, by getting username from Basic Auth(Security Context Holder)


### IMPLEMENTING AUTHENTICATION ON JOURNAL CONTROLLER
- updated all methods to add/get/update/delete journal entries to fetch username from SecurityContextHolder. get context() getAuthentication.getName()
- Making sure that user A cannot get/update/delete user B

