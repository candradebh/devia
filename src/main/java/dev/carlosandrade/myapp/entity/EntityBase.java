package dev.carlosandrade.myapp.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityBase {

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}

