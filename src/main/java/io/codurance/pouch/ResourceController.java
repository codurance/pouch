package io.codurance.pouch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
