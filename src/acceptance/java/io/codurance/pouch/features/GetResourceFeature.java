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

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetResourceFeature {

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
    public void shouldGetOneResource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resourceOne = new DatabaseHelper.Resource(
                randomUUID,
                currentTimestamp,
                "Spring Data JDBC",
                "https://spring.io/projects/spring-data-jdbc");

        databaseHelper.insertResource(resourceOne);

        when().get("/resources/" + randomUUID.toString())
                .then()
                .statusCode(SC_OK)
                .contentType(JSON)
                .body("id", equalTo(randomUUID.toString()))
                .body("added", equalTo(currentTimestamp.toString()))
                .body("title", equalTo("Spring Data JDBC"))
                .body("url", equalTo("https://spring.io/projects/spring-data-jdbc"));
    }

    @Test
    public void shouldProduceServerErrorForNonExistingResource() {
        var randomUUID = randomUUID();
        when().get("/resources/" + randomUUID.toString())
                .then()
                .statusCode(SC_NOT_FOUND)
                .body(isEmptyString());
    }
}
