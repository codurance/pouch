package io.codurance.pouch.features;

import io.codurance.pouch.PouchApiApplication;
import io.codurance.pouch.RestAssuredConfiguration;
import io.codurance.pouch.database.DatabaseHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ListResourcesFeature {

    @LocalServerPort
    private int serverPort;

    @Value("${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    private DatabaseHelper databaseHelper;

    @Before
    public void setUp() {
        databaseHelper = new DatabaseHelper(jdbcUrl);
        databaseHelper.clearResources();

        RestAssuredConfiguration.configure(serverPort);
    }

    @Test
    public void shouldListResources() {
        var currentTimestamp = now();
        UUID randomUuidOne = randomUUID();
        var resourceOne = new DatabaseHelper.Resource(
                randomUuidOne,
                currentTimestamp,
                "Spring Data JDBC",
                "https://spring.io/projects/spring-data-jdbc");
        UUID randomUuidTwo = randomUUID();
        var resourceTwo = new DatabaseHelper.Resource(
                randomUuidTwo,
                currentTimestamp,
                "SQL Fiddle",
                "http://sqlfiddle.com/");
        UUID randomUuidThree = randomUUID();
        var resourceThree = new DatabaseHelper.Resource(
                randomUuidThree,
                currentTimestamp,
                "PostgreSQL: The world's most advanced open source database",
                "https://www.postgresql.org/");

        databaseHelper.insertResource(resourceOne);
        databaseHelper.insertResource(resourceTwo);
        databaseHelper.insertResource(resourceThree);

        when().get("/resources")
                .then()
                .statusCode(SC_OK)
                .contentType(JSON)
                .body("[0].id", equalTo(randomUuidOne.toString()))
                .body("[0].added", equalTo(currentTimestamp.toString()))
                .body("[0].title", equalTo("Spring Data JDBC"))
                .body("[0].url", equalTo("https://spring.io/projects/spring-data-jdbc"))
                .body("[1].id", equalTo(randomUuidTwo.toString()))
                .body("[1].added", equalTo(currentTimestamp.toString()))
                .body("[1].title", equalTo("SQL Fiddle"))
                .body("[1].url", equalTo("http://sqlfiddle.com/"))
                .body("[2].id", equalTo(randomUuidThree.toString()))
                .body("[2].added", equalTo(currentTimestamp.toString()))
                .body("[2].title", equalTo("PostgreSQL: The world's most advanced open source database"))
                .body("[2].url", equalTo("https://www.postgresql.org/"));
    }
}
