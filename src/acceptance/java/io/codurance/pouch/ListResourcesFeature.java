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
        var resourceOne = new DatabaseHelper.Resource(
                1,
                "2018-12-05 16:01:00.0",
                "Spring Data JDBC",
                "https://spring.io/projects/spring-data-jdbc");
        var resourceTwo = new DatabaseHelper.Resource(
                2,
                "2018-12-05 16:02:00.0",
                "SQL Fiddle",
                "http://sqlfiddle.com/");
        var resourceThree = new DatabaseHelper.Resource(
                3,
                "2018-12-05 16:03:00.0",
                "PostgreSQL: The world's most advanced open source database",
                "https://www.postgresql.org/");

        databaseHelper.insertResource(resourceOne);
        databaseHelper.insertResource(resourceTwo);
        databaseHelper.insertResource(resourceThree);

        when().get("/resources")
                .then()
                .statusCode(SC_OK)
                .contentType(JSON)
                .body("[0].added", equalTo("2018-12-05 16:01:00.0"))
                .body("[0].title", equalTo("Spring Data JDBC"))
                .body("[0].url", equalTo("https://spring.io/projects/spring-data-jdbc"))
                .body("[1].added", equalTo("2018-12-05 16:02:00.0"))
                .body("[1].title", equalTo("SQL Fiddle"))
                .body("[1].url", equalTo("http://sqlfiddle.com/"))
                .body("[2].added", equalTo("2018-12-05 16:03:00.0"))
                .body("[2].title", equalTo("PostgreSQL: The world's most advanced open source database"))
                .body("[2].url", equalTo("https://www.postgresql.org/"));
    }
}
