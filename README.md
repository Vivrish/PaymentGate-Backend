# Payment Gate

# Author: Nikolai Chaunin (FIT ČVUT)


# Overview
It's a Java Springboot project that provides backend for a money transfer system. Users can create, edit or delete their profiles, bank cards or make transactions to other users. 

# Structure 
The project is divided into three layers: persistance, application and presenation.

Postgres database is set up using docker.compose in an isolated container. Database is connected to the persistance layer using JPA and hibernate. 
Application layer takes care of all business logic and is connected to the presentation layer, which consists of RESTful API with OpenAPI documentation. All connections between layers are loosely coupled by using dependency injections. Presentation layer is supposed to communicate to the client side via HTTP requests. Presentation layer also actively uses Data Tranfer Objects. My own basic implementation of the client side is located in different repository (see PaymentGate-Frontend). 
The **project** includes unit, integration and End to End tests. Intergration and unit tests use Mock Beans for isolation purposes. All tests use separate database.

# Resourses
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




