package io.codurance.pouch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.time.Instant.*;
import static java.util.Arrays.asList;
import static java.util.UUID.*;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

        assertThat(resourceController.listAll(), is(resourceList));
    }

    @Test
    void return_one_specified_resource() {
        var randomUUID = randomUUID();
        var currentTimestamp = now();
        var resource = new Resource(randomUUID, currentTimestamp, "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc");
        when(resourceRepository.findById(randomUUID)).thenReturn(Optional.of(resource));

        var responseEntity = resourceController.getById(randomUUID);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_OK)));
        assertThat(responseEntity.getBody(), is(resource));
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
        var input = new ResourceDTO("Baeldung | Java, Spring and Web Development tutorials", "https://www.baeldung.com");
        var resource = new Resource(randomUUID, currentTimestamp, "Baeldung | Java, Spring and Web Development tutorials", "https://www.baeldung.com");
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        var responseEntity = resourceController.add(input);

        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_CREATED)));
        assertThat(responseEntity.getBody(), is(resource));
    }
}
