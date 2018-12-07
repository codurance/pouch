import io.codurance.pouch.PouchApiApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthCheck {

    @LocalServerPort
    private int serverPort;

    @Test
    void returns_200_with_expected_health_check_token_from_database() {

        RestAssured.port = serverPort;

        when().get("/healthcheck")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .body("status", equalTo("OK"));
    }
}
