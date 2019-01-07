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

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;

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
        Map<String, String> resourceToAdd = new HashMap<>();
        resourceToAdd.put("added", "2018-12-18 12:30:00.0");
        resourceToAdd.put("title", "Stack Overflow - Where Developers Learn, Share, & Build Careers");
        resourceToAdd.put("url", "https://stackoverflow.com");

        given().contentType(JSON)
                .body(resourceToAdd)
                .when()
                .post("/resources")
                .peek()
                .then()
                .body("added", equalTo("2018-12-18 12:30:00.0"))
                .body("title", equalTo("Stack Overflow - Where Developers Learn, Share, & Build Careers"))
                .body("url", equalTo("https://stackoverflow.com"))
                .statusCode(SC_CREATED);
    }
}
