Pouch RESTful API Service
=========================

| Build Status  |
|     :---:     |
| [![Build Status](https://travis-ci.org/codurance/pouch.svg?branch=master)](https://travis-ci.org/codurance/pouch) |

## Project info
**Pouch** is meant to be a service for managing a reading list of learning resources.
**Pouch RESTful API Service** is the back-end part of this service that stores and retrieves resources for the users of the app, accessed via HTTP request methods.

#### Minimum Viable Product (MVP)
As a first iteration, the primary goal is to build a minimum viable product (MVP) of the intended web service, starting with the back-end API.

## Development

#### Running the database container
In order to run the acceptance tests locally, you will need to run the PostgreSQL container locally. Run the following
command when in the root of the project:

    $ docker-compose up -d
    
To run all the tests (from command-line) use the command below:

    $ ./gradlew check

To run the api service locally and simultaneously set-up the database with the correct schema and data, run the below command:

    $ ./gradlew bootRun
      
While the api service is running locally, you can open the following URL in your browser to inspect the api and interact with the endpoints:

[http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html)

## Frameworks, Tools and Services used in this project
|      |      |
| :--- | :--- |
| **Java 11** | OpenJDK implementation of the Java SE 11 Platform |
| **Gradle 5.0** | build automation and project configuration system |
| **Spring Boot 2.1.0** | convention-over-configuration app framework |
| **Spring Boot Web Starter** | Spring MVC, embedded Tomcat container |
| **Spring Boot JDBC Starter** | JDBC with the HikariCP connection pool |
| **Spring Data JDBC** | simple, limited, opinionated ORM |
| **Spring Boot Test Starter** | support for JUnit, Hamcrest and Mockito |
| **REST Assured** | Java DSL for easy testing of REST services |
| **Docker** | operating-system-level virtualization |
| **PostgreSQL** | Java JDBC driver for PostgreSQL database |
| **Flyway Core** | database migration tool |
| **Swagger 2** | JSON API documentation |
| **Springfox Swagger UI** | Swagger UI for Spring based applications |
| **Travis CI** | hosted, distributed continuous integration service |
| **Heroku** | cloud platform as a service for application hosting |
| **Heroku Gradle Plugin** | Gradle plugin for deploying JAR files to Heroku |
| **Uptime Robot** | website uptime monitoring |