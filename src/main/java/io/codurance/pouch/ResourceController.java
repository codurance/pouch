package io.codurance.pouch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

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
    ResponseEntity<Resource> getById(@PathVariable UUID id) {
        return resourceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
    }

    @PostMapping("/resources")
    ResponseEntity<Resource> add(@RequestBody ResourceDTO input) {
        var resource = resourceRepository.save(createResourceFrom(input));
        return new ResponseEntity<>(resource, CREATED);
    }

    @DeleteMapping("/resources/{id}")
    ResponseEntity<Resource> remove(@PathVariable UUID id) {
        resourceRepository.deleteById(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("/resources/{id}")
    ResponseEntity<Resource> updateById(@PathVariable UUID id, @RequestBody ResourceDTO input) {
        Optional<Resource> targetResource = resourceRepository.findById(id);
        if (!targetResource.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        var resourceToUpdate = targetResource.get();
        updateResourceFrom(resourceToUpdate, input);

        var resource = resourceRepository.save(resourceToUpdate);
        return new ResponseEntity<>(resource, OK);
    }

    private void updateResourceFrom(Resource resourceToUpdate, @RequestBody ResourceDTO input) {
        resourceToUpdate.setTitle(input.getTitle());
        resourceToUpdate.setUrl(input.getUrl());
    }

    private Resource createResourceFrom(ResourceDTO resourceDTO) {
        return new Resource(UUID.randomUUID(), Instant.now(), resourceDTO.getTitle(), resourceDTO.getUrl());
    }
}
