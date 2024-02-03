# Payment Gate

# Author: Nikolai Chaunin (FIT ČVUT)


# Overview
It's a Java Springboot project that provides backend for a money transfer system. Users can create, edit or delete their profiles, bank cards or make transactions to other users. 

# Structure 
The project is divided into three layers: persistence, application and presenation.

Postgres database is set up using docker.compose in an isolated container. Database is connected to the persistence layer using JPA and hibernate. 
Application layer takes care of all business logic and is connected to the presentation layer, which consists of RESTful API with OpenAPI documentation. All connections between layers are loosely coupled by using dependency injections. Presentation layer is supposed to communicate to the client side via HTTP requests. Presentation layer also actively uses Data Transfer Objects. My own basic implementation of the client side is located in different repository (see PaymentGate-Frontend). 
The **project** includes unit, integration and End to End tests. Integration and unit tests use Mock Beans for isolation purposes. All tests use separate database.

# Resources
Java

Gradle

Spring Boot

Spring Web

Hibernate

Docker

PostgreSQL

JPQL

Junit

Mockito

Swagger 

# Features
OOP

Three tier architecture

Dependency injection

RESTful API

OpenAPI

DTO

Unit/Integration/E2E tests


<img width="1280" alt="Screenshot 2024-02-03 at 22 52 41" src="https://github.com/Vivrish/PaymentGate-Backend/assets/64595636/8d74611d-6395-4497-aaf0-4f19d27f4910">

<img width="1280" alt="Screenshot 2024-02-03 at 22 52 33" src="https://github.com/Vivrish/PaymentGate-Backend/assets/64595636/2583a0e5-123c-44d6-8cac-b196274e8a92">

<img width="1280" alt="Screenshot 2024-02-03 at 22 52 07" src="https://github.com/Vivrish/PaymentGate-Backend/assets/64595636/e68d0394-082e-4f05-a274-3d94a399a995">



