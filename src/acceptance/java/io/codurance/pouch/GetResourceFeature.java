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
import static io.restassured.http.ContentType.JSON;
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
        var resourceOne = new DatabaseHelper.Resource(
                1,
                "2018-12-05 16:01:00.0",
                "Spring Data JDBC",
                "https://spring.io/projects/spring-data-jdbc");

        databaseHelper.insertResource(resourceOne);

        when().get("/resources/1")
                .then()
                .statusCode(SC_OK)
                .contentType(JSON)
                .body("id", equalTo(1))
                .body("added", equalTo("2018-12-05 16:01:00.0"))
                .body("title", equalTo("Spring Data JDBC"))
                .body("url", equalTo("https://spring.io/projects/spring-data-jdbc"));
    }

    @Test
    public void shouldProduceServerErrorForNonExistingResource() {
        when().get("/resources/11")
                .then()
                .statusCode(SC_NOT_FOUND)
                .body(isEmptyString());
    }
}
