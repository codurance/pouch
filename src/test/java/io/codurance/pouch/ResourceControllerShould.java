package io.codurance.pouch;

import io.codurance.pouch.api.ResourceController;
import io.codurance.pouch.api.ResourceRequestDTO;
import io.codurance.pouch.api.ResourceResponseDTO;
import io.codurance.pouch.domain.Resource;
import io.codurance.pouch.domain.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.valueOf;

class ResourceControllerShould {

    private ResourceRepository resourceRepository;
    private ResourceController resourceController;

    @BeforeEach
    void setUp() {
        resourceRepository = mock(ResourceRepository.class);
        resourceController = new ResourceController(resourceRepository);
    }

    @Test
    void return_list_of_all_resources() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resourceList = asList(
                new Resource(randomUUID, currentTimestamp, "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc"),
                new Resource(randomUUID, currentTimestamp, "SQL Fiddle", "http://sqlfiddle.com/"),
                new Resource(randomUUID, currentTimestamp, "PostgreSQL: The world''s most advanced open source database", "https://www.postgresql.org/"));
        when(resourceRepository.findAll()).thenReturn(resourceList);
        var responseList = asList(
                new ResourceResponseDTO(randomUUID, currentTimestamp, "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc", false),
                new ResourceResponseDTO(randomUUID, currentTimestamp, "SQL Fiddle", "http://sqlfiddle.com/", false),
                new ResourceResponseDTO(randomUUID, currentTimestamp, "PostgreSQL: The world''s most advanced open source database", "https://www.postgresql.org/", false));

        assertThat(resourceController.listAll(), is(responseList));
    }

    @Test
    void return_one_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc");
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));
        var response = new ResourceResponseDTO(randomUUID, currentTimestamp, "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc", false);

        var responseEntity = resourceController.getById(randomUUID);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_OK)));
        assertThat(responseEntity.getBody(), is(response));
    }

    @Test
    void return_empty_body_for_non_existing_specified_resource() {
        var randomUUID = randomUUID();
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.empty());

        var responseEntity = resourceController.getById(randomUUID);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_NOT_FOUND)));
        assertThat(responseEntity.getBody(), nullValue());
    }

    @Test
    void return_newly_added_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var input = new ResourceRequestDTO("Baeldung | Java, Spring and Web Development tutorials", "https://www.baeldung.com");
        var resource = new Resource(randomUUID, currentTimestamp, "Baeldung | Java, Spring and Web Development tutorials", "https://www.baeldung.com");
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);
        var response = new ResourceResponseDTO(randomUUID, currentTimestamp, "Baeldung | Java, Spring and Web Development tutorials", "https://www.baeldung.com", false);

        var responseEntity = resourceController.add(input);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_CREATED)));
        assertThat(responseEntity.getBody(), is(response));
    }

    @Test
    void delete_one_specified_resource() {
        var randomUUID = randomUUID();

        var responseEntity = resourceController.remove(randomUUID);

        verify(resourceRepository).deleteById(randomUUID);
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_NO_CONTENT)));
    }

    @Test
    void update_one_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var input = new ResourceRequestDTO("JetBrains: Developer Tools for Professionals and Teams", "https://www.jetbrains.com/");
        var resource = new Resource(randomUUID, currentTimestamp, "Baeldung | Java, Spring and Web Development tutorials", "https://www.baeldung.com");
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);
        var response = new ResourceResponseDTO(randomUUID, currentTimestamp, "JetBrains: Developer Tools for Professionals and Teams", "https://www.jetbrains.com/", false);

        var responseEntity = resourceController.updateById(randomUUID, input);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_OK)));
        assertThat(responseEntity.getBody(), is(response));
    }

    @Test
    void on_update_return_empty_body_for_non_existing_specified_resource() {
        var randomUUID = randomUUID();
        var input = new ResourceRequestDTO("JetBrains: Developer Tools for Professionals and Teams", "https://www.jetbrains.com/");
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.empty());

        var responseEntity = resourceController.updateById(randomUUID, input);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_NOT_FOUND)));
        assertThat(responseEntity.getBody(), nullValue());
    }

    @Test
    void return_one_specific_resource_specified_by_title() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "Home | The Met", "https://www.met.police.uk/");
        when(resourceRepository.findByTitle(resource.getTitle())).thenReturn(asList(resource));

        var responseList = asList(
                new ResourceResponseDTO(randomUUID, currentTimestamp, "Home | The Met", "https://www.met.police.uk/", false));

        assertThat(resourceController.findByTitle(resource.getTitle()), is(responseList));
    }
}
