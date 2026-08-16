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

401 Unauthorized: Missing or invalid authentication credentials.

403 Forbidden: Authenticated, but lacks required permissions. (Not authorised)

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
