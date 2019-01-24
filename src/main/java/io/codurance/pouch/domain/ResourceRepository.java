package io.codurance.pouch.domain;

import io.codurance.pouch.domain.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ResourceRepository extends CrudRepository<Resource, UUID> {}
