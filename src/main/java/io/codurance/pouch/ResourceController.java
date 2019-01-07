package io.codurance.pouch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class ResourceController {

    private final ResourceRepository resourceRepository;

    @Autowired
    ResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/resources")
    Iterable<Resource> listAll() {
        return resourceRepository.findAll();
    }

    @GetMapping("/resources/{id}")
    @ResponseBody
    ResponseEntity<Resource> getById(@PathVariable Integer id) {
        return resourceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
    }

    @PostMapping("/resources")
    ResponseEntity<Resource> add(@RequestBody Resource input) {
        var resource = resourceRepository.save(input);
        return new ResponseEntity<>(resource, CREATED);
    }
}
