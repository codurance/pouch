package io.codurance.pouch;

import io.codurance.pouch.api.ResourceFavouriteController;
import io.codurance.pouch.domain.Resource;
import io.codurance.pouch.domain.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.valueOf;

class ResourceFavouriteControllerShould {

    private ResourceRepository resourceRepository;
    private ResourceFavouriteController resourceFavouriteController;

    @BeforeEach
    void setUp() {
        resourceRepository = mock(ResourceRepository.class);
        resourceFavouriteController = new ResourceFavouriteController(resourceRepository);
    }

    @Test
    void add_favourite_status_to_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "GitHub", "https://github.com");
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        var responseEntity = resourceFavouriteController.addFavouriteById(randomUUID);

        assertThat(resource.isFavourite(), is(true));
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_NO_CONTENT)));
    }

    @Test
    void remove_favourite_status_from_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "GitHub", "https://github.com");
        resource.setAsFavourite();
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        var responseEntity = resourceFavouriteController.removeFavouriteById(randomUUID);

        assertThat(resource.isFavourite(), is(false));
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_NO_CONTENT)));
    }
}
