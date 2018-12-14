package io.codurance.pouch;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResourceControllerShould {

    @Test
    void return_list_of_all_resources() {
        ResourceRepository resourceRepository = mock(ResourceRepository.class);

        List<Resource> resourceList = asList(
                new Resource(1, "2018-12-05 16:01:00", "Spring Data JDBC", "https://spring.io/projects/spring-data-jdbc"),
                new Resource(2, "2018-12-05 16:02:00", "SQL Fiddle", "http://sqlfiddle.com/"),
                new Resource(3, "2018-12-05 16:03:00", "PostgreSQL: The world''s most advanced open source database", "https://www.postgresql.org/"));

        when(resourceRepository.findAll()).thenReturn(resourceList);

        ResourceController resourceController = new ResourceController(resourceRepository);

        assertThat(resourceController.findAll(), Matchers.is(resourceList));
    }
}
