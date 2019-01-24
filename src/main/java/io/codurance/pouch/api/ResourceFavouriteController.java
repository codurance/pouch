package io.codurance.pouch.api;

import io.codurance.pouch.domain.Resource;
import io.codurance.pouch.domain.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
public
class ResourceFavouriteController {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceFavouriteController(ResourceRepository ResourceRepository) {
        this.resourceRepository = ResourceRepository;
    }

    @PutMapping("/resources/{id}/favourite")
    public ResponseEntity<ResourceResponseDTO> addFavouriteById(@PathVariable UUID id) {
        Optional<Resource> targetResource = resourceRepository.findById(id);
        if (!targetResource.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        var resourceToFavourite = targetResource.get();
        if (!resourceToFavourite.isFavourite()) {
            resourceToFavourite.setAsFavourite();
        }

        resourceRepository.save(resourceToFavourite);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("/resources/{id}/favourite")
    public ResponseEntity<ResourceResponseDTO> removeFavouriteById(@PathVariable UUID id) {
        Optional<Resource> targetResource = resourceRepository.findById(id);
        if (!targetResource.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        var resourceToUnfavourite = targetResource.get();
        if (resourceToUnfavourite.isFavourite()) {
            resourceToUnfavourite.removeFavourite();
        }

        resourceRepository.save(resourceToUnfavourite);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
