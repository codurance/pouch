package io.codurance.pouch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

public abstract class Entity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isExisting = false;

    public Entity() {
        // default constructor needed for JSON deserialization
    }

    Entity(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return !isExisting;
    }
}
