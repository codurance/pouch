package io.codurance.pouch;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static io.restassured.RestAssured.when;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

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
                "Hacker Dawn",
                "https://hackerdawn.com/");

        databaseHelper.insertResource(resourceToFavourite);

        when().put("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_NO_CONTENT);

        assertThat(databaseHelper.readResource(randomUUID).get().isFavourite(), is(true));
    }

    @Test
    public void shouldRemoveFavouriteStatusOnResource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resourceToFavourite = new DatabaseHelper.Resource(
                randomUUID,
                currentTimestamp,
                "Hacker Noon",
                "https://hackernoon.com/",
                true);

        databaseHelper.insertResource(resourceToFavourite);

        when().delete("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_NO_CONTENT);

        assertThat(databaseHelper.readResource(randomUUID).get().isFavourite(), is(false));
    }

    @Test
    public void shouldProduceClientErrorForAddingFavouriteStatusToNonExistingResource() {
        var randomUUID = randomUUID();

        when().put("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void shouldProduceClientErrorForRemovingFavouriteStatusOnNonExistingResource() {
        var randomUUID = randomUUID();

        when().delete("/resources/" + randomUUID.toString() + "/favourite")
                .then()
                .statusCode(SC_NOT_FOUND);
    }
}
