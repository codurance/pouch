package io.codurance.pouch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class ResourceController {

    private final ResourceRepository resourceRepository;

    @Autowired
    ResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/resources")
    Iterable<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @GetMapping("/resources/{id}")
    @ResponseBody
    ResponseEntity<Resource> findById(@PathVariable Integer id) {
        return resourceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
    }
}
