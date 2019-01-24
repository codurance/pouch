package io.codurance.pouch.domain;

import io.codurance.pouch.domain.Health;
import org.springframework.data.repository.CrudRepository;

public interface HealthRepository extends CrudRepository<Health, Integer> {}