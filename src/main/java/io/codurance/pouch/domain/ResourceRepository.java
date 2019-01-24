package io.codurance.pouch.domain;

import io.codurance.pouch.domain.Resource;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResourceRepository extends CrudRepository<Resource, UUID> {

    @Query("SELECT r.id, r.added, r.title, r.url, r.favourite FROM Resource r WHERE r.title=:title")
    List<Resource> findByTitle(@Param("title") String title);
}
