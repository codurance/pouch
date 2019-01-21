package io.codurance.pouch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.valueOf;

class ResourceFavouriteControllerShould {

    private ResourceRepository ResourceRepository;
    private ResourceFavouriteController resourceFavouriteController;

    @BeforeEach
    void setUp() {
        ResourceRepository = mock(ResourceRepository.class);
        resourceFavouriteController = new ResourceFavouriteController(ResourceRepository);
    }

    @Test
    void add_favourite_status_to_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "GitHub", "https://github.com");
        when(ResourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));
        when(ResourceRepository.save(any(Resource.class))).thenReturn(resource);

        var responseEntity = resourceFavouriteController.addFavouriteById(randomUUID);

        verify(ResourceRepository).findById(randomUUID);
        verify(ResourceRepository).save(resource);
        assertThat(resource.isFavourite(), is(true));
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_OK)));
    }

    @Test
    void remove_favourite_status_from_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "GitHub", "https://github.com");
        resource.setFavourite(true);
        when(ResourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));
        when(ResourceRepository.save(any(Resource.class))).thenReturn(resource);

        var responseEntity = resourceFavouriteController.removeFavouriteById(randomUUID);

        verify(ResourceRepository).findById(randomUUID);
        verify(ResourceRepository).save(resource);
        assertThat(resource.isFavourite(), is(false));
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_OK)));
    }
}
