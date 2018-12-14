package io.codurance.pouch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static java.util.Arrays.asList;

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
    public void shouldListResources() throws JsonProcessingException, JSONException {
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

        var actual = when().get("/resources")
                .getBody()
                .asString();

        var objectMapper = new ObjectMapper();

        var expected = objectMapper.writeValueAsString(asList(resourceOne, resourceTwo, resourceThree));

        JSONAssert.assertEquals(expected, actual, false);
    }
}
