package io.codurance.pouch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.isEmptyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class FavouriteStatusFeature {

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
    public void shouldAddFavouriteStatusToResource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resourceToFavourite = new DatabaseHelper.Resource(
                randomUUID,
                currentTimestamp,
                "Hacker Noon",
                "https://hackernoon.com/");

        databaseHelper.insertResource(resourceToFavourite);

        when().put("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_OK);
    }

    @Test
    public void shouldRemoveFavouriteStatusOnResource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resourceToFavourite = new DatabaseHelper.Resource(
                randomUUID,
                currentTimestamp,
                "Hacker Noon",
                "https://hackernoon.com/");

        resourceToFavourite.setFavourite(true);
        databaseHelper.insertResource(resourceToFavourite);

        when().delete("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_OK);
    }

    @Test
    public void shouldProduceClientErrorForAddingFavouriteStatusToNonExistingResource() {
        var randomUUID = randomUUID();

        when().put("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_NOT_FOUND)
                .body(isEmptyString());
    }

    @Test
    public void shouldProduceClientErrorForRemovingFavouriteStatusOnNonExistingResource() {
        var randomUUID = randomUUID();

        when().delete("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_NOT_FOUND)
                .body(isEmptyString());
    }
}
