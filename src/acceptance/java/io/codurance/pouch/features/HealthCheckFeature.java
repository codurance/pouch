package io.codurance.pouch.features;

import io.codurance.pouch.PouchApiApplication;
import io.codurance.pouch.RestAssuredConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class HealthCheckFeature {

    @LocalServerPort
    private int serverPort;

    @Before
    public void setUp() {
        RestAssuredConfiguration.configure(serverPort);
    }

    @Test
    public void shouldPerformHealthCheck() {
        when().get("/healthcheck")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .body("status", equalTo("OK"));
    }
}
