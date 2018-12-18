package io.codurance.pouch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
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
        var resourceList = asList(
                new Resource(1, "2018-12-05 16:01:00", "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc"),
                new Resource(2, "2018-12-05 16:02:00", "SQL Fiddle", "http://sqlfiddle.com/"),
                new Resource(3, "2018-12-05 16:03:00", "PostgreSQL: The world''s most advanced open source database", "https://www.postgresql.org/"));

        when(resourceRepository.findAll()).thenReturn(resourceList);

        assertThat(resourceController.findAll(), is(resourceList));
    }

    @Test
    void return_one_specified_resource() {
        var resource = new Resource(1, "2018-12-05 16:01:00", "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc");

        when(resourceRepository.findById(1)).thenReturn(Optional.of(resource));

        var responseEntity = resourceController.findById(1);
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_OK)));
        assertThat(responseEntity.getBody(), is(resource));
    }

    @Test
    void return_empty_body_for_non_existing_specified_resource() {
        when(resourceRepository.findById(11)).thenReturn(Optional.empty());

        var responseEntity = resourceController.findById(11);
        assertThat(responseEntity.getStatusCode(), is(valueOf(SC_NOT_FOUND)));
        assertThat(responseEntity.getBody(), nullValue());
    }
}
