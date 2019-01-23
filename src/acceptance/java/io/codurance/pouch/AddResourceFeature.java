package io.codurance.pouch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AddResourceFeature {

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
    public void shouldAddOneResource() {
        Map<String, String> resourceToAdd = new HashMap<>(){{
            put("title", "Stack Overflow - Where Developers Learn, Share, & Build Careers");
            put("url", "https://stackoverflow.com");
        }};

        var response = given()
                .contentType(JSON)
                .body(resourceToAdd)
                .when()
                .post("/resources")
                .peek()
                .then()
                .body("added", notNullValue())
                .body("title", equalTo("Stack Overflow - Where Developers Learn, Share, & Build Careers"))
                .body("url", equalTo("https://stackoverflow.com"))
                .statusCode(SC_CREATED)
                .extract()
                .response();

        var resource = response.getBody().as(DatabaseHelper.Resource.class);
        assertThat(databaseHelper.readResource(resource.getId()), is(Optional.of(resource)));
    }
}
