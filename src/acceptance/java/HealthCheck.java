import io.codurance.pouch.PouchApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes= PouchApiApplication.class)
class HealthCheck {

    @Test
    void returns_200_with_expected_health_check_token() {

        when().
                get("/healthcheck").
        then().
                statusCode(200).
                body("health.check", equalTo("alive"));
    }
}
