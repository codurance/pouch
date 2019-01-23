package io.codurance.pouch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static io.restassured.RestAssured.when;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteResourceFeature {

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
    public void shouldDeleteOneResource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resourceToDelete = new DatabaseHelper.Resource(
                randomUUID,
                currentTimestamp,
                "JUnit 5",
                "https://junit.org/junit5");

        databaseHelper.insertResource(resourceToDelete);

        when().delete("/resources/" + randomUUID.toString())
                .then()
                .statusCode(SC_NO_CONTENT)
                .body(isEmptyString());

        assertThat(databaseHelper.readResource(randomUUID), is(Optional.empty()));
    }
}
