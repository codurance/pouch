package io.codurance.pouch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class UpdateResourceFeature {

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
    public void shouldUpdateOneResource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now().truncatedTo(ChronoUnit.MILLIS);
        var resourceToUpdate = new DatabaseHelper.Resource(
                randomUUID,
                currentTimestamp,
                "Unwanted Title",
                "https://wrong-url.wtf/");

        databaseHelper.insertResource(resourceToUpdate);

        Map<String, String> updatedResourceData = new HashMap<>(){{
            put("title", "Asciidoctor | A fast, open source text processor and publishing toolchain");
            put("url", "https://asciidoctor.org/");
        }};

        var response = given()
                .contentType(JSON)
                .body(updatedResourceData)
                .when()
                .put("/resources/" + randomUUID.toString())
                .peek()
                .then()
                .body("added", notNullValue())
                .body("title", equalTo("Asciidoctor | A fast, open source text processor and publishing toolchain"))
                .body("url", equalTo("https://asciidoctor.org/"))
                .statusCode(SC_OK)
                .extract()
                .response();

        var resource = response.getBody().as(DatabaseHelper.Resource.class);
        assertThat(databaseHelper.readResource(resource.getId()), is(Optional.of(resource)));
    }

    @Test
    public void shouldProduceClientErrorForNonExistingResource() {
        var randomUUID = randomUUID();

        Map<String, String> updatedResourceData = new HashMap<>(){{
            put("title", "Asciidoctor | A fast, open source text processor and publishing toolchain");
            put("url", "https://asciidoctor.org/");
        }};

        given().contentType(JSON)
                .body(updatedResourceData)
                .when()
                .put("/resources/" + randomUUID.toString())
                .peek()
                .then()
                .statusCode(SC_NOT_FOUND)
                .body(isEmptyString());
    }
}
