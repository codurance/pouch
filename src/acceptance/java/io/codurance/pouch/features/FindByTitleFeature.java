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
public class FindByTitleFeature {

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
    public void shouldFindResourceByTitle() {
        var currentTimestamp = now();
        UUID randomUuidOne = randomUUID();
        var resourceOne = new DatabaseHelper.Resource(
                randomUuidOne,
                currentTimestamp,
                "BBC - Home",
                "https://www.bbc.co.uk/");
        UUID randomUuidTwo = randomUUID();
        var resourceTwo = new DatabaseHelper.Resource(
                randomUuidTwo,
                currentTimestamp,
                "Welcome to GOV.UK",
                "https://www.gov.uk/");
        UUID randomUuidThree = randomUUID();
        var resourceThree = new DatabaseHelper.Resource(
                randomUuidThree,
                currentTimestamp,
                "European Union - EUROPA | European Union",
                "https://europa.eu/european-union/");

        databaseHelper.insertResource(resourceOne);
        databaseHelper.insertResource(resourceTwo);
        databaseHelper.insertResource(resourceThree);

        when().get("/resources/?title=" + resourceThree.getTitle())
                .then()
                .statusCode(SC_OK)
                .contentType(JSON)
                .body("[0].id", equalTo(randomUuidThree.toString()))
                .body("[0].added", equalTo(currentTimestamp.toString()))
                .body("[0].title", equalTo("European Union - EUROPA | European Union"))
                .body("[0].url", equalTo("https://europa.eu/european-union/"));
    }
}
