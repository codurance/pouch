package io.codurance.pouch;

import org.springframework.data.repository.CrudRepository;

interface HealthRepository extends CrudRepository<Health, Integer> {}