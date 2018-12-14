package io.codurance.pouch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    HealthRepository healthRepository;

    @GetMapping("/healthcheck")
    @ResponseBody
    public ResponseEntity<Health> checkHealth() {

        return healthRepository.findById(1)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(502).build());
    }
}
