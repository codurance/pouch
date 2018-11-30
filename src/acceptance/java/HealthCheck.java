import io.codurance.pouch.PouchApiApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes = PouchApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthCheck {

    @LocalServerPort
    private int serverPort;

    @Test
    void returns_200_with_expected_health_check_token() {

        RestAssured.port = serverPort;

        when().get("/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }
}
