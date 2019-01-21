package io.codurance.pouch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
class ResourceFavouriteController {

    private final ResourceRepository ResourceRepository;

    @Autowired
    ResourceFavouriteController(ResourceRepository ResourceRepository) {
        this.ResourceRepository = ResourceRepository;
    }

    @PutMapping("/resources/{id}/favourite")
    ResponseEntity<Resource> addFavouriteById(@PathVariable UUID id) {
        Optional<Resource> targetResource = ResourceRepository.findById(id);
        if (!targetResource.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        var resourceToFavourite = targetResource.get();
        if (!resourceToFavourite.isFavourite()) {
            resourceToFavourite.setFavourite(true);
        }

        ResourceRepository.save(resourceToFavourite);
        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping("/resources/{id}/favourite")
    ResponseEntity<Resource> removeFavouriteById(@PathVariable UUID id) {
        Optional<Resource> targetResource = ResourceRepository.findById(id);
        if (!targetResource.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        var resourceToUnfavourite = targetResource.get();
        if (resourceToUnfavourite.isFavourite()) {
            resourceToUnfavourite.setFavourite(false);
        }

        ResourceRepository.save(resourceToUnfavourite);
        return ResponseEntity.status(OK).build();
    }
}
