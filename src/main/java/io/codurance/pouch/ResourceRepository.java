package io.codurance.pouch;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface ResourceRepository extends CrudRepository<Resource, UUID> {}
