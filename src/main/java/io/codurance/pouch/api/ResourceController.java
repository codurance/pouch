package io.codurance.pouch.api;

import io.codurance.pouch.domain.Resource;
import io.codurance.pouch.domain.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.*;

@RestController
public
class ResourceController {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/resources")
    public Iterable<ResourceResponseDTO> listAll() {
        Iterable<Resource> allResources = resourceRepository.findAll();
        return StreamSupport.stream(allResources.spliterator(), false)
                .map(ResourceResponseDTO::createResponseDTOFrom)
                .collect(Collectors.toList());
    }

    @GetMapping("/resources/{id}")
    @ResponseBody
    public ResponseEntity<ResourceResponseDTO> getById(@PathVariable UUID id) {
        return resourceRepository.findById(id)
                .map(resource -> ResponseEntity.ok(ResourceResponseDTO.createResponseDTOFrom(resource)))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
    }

    @PostMapping("/resources")
    public ResponseEntity<ResourceResponseDTO> add(@RequestBody ResourceRequestDTO input) {
        var resource = resourceRepository.save(createResourceFrom(input));
        var response = ResourceResponseDTO.createResponseDTOFrom(resource);
        return new ResponseEntity<>(response, CREATED);
    }

    @DeleteMapping("/resources/{id}")
    public ResponseEntity<ResourceResponseDTO> remove(@PathVariable UUID id) {
        resourceRepository.deleteById(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("/resources/{id}")
    public ResponseEntity<ResourceResponseDTO> updateById(@PathVariable UUID id, @RequestBody ResourceRequestDTO input) {
        Optional<Resource> targetResource = resourceRepository.findById(id);
        if (!targetResource.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        var resourceToUpdate = targetResource.get();
        updateResourceFrom(resourceToUpdate, input);

        var resource = resourceRepository.save(resourceToUpdate);
        var response = ResourceResponseDTO.createResponseDTOFrom(resource);
        return new ResponseEntity<>(response, OK);
    }

    private void updateResourceFrom(Resource resourceToUpdate, @RequestBody ResourceRequestDTO input) {
        resourceToUpdate.setTitle(input.getTitle());
        resourceToUpdate.setUrl(input.getUrl());
    }

    private Resource createResourceFrom(ResourceRequestDTO resourceRequestDTO) {
        return new Resource(UUID.randomUUID(), Instant.now().truncatedTo(ChronoUnit.MILLIS), resourceRequestDTO.getTitle(), resourceRequestDTO.getUrl());
    }
}
