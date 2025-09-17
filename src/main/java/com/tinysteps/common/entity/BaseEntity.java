package com.tinysteps.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.UUID;

/**
 * Base entity class providing soft delete functionality and audit fields.
 * All entities should extend this class to ensure consistent soft delete behavior.
 * 
 * Soft Delete Strategy:
 * - Uses EntityStatus enum (ACTIVE, INACTIVE) instead of boolean for extensibility
 * - Tracks deletion timestamp and user who performed the deletion
 * - Provides JPA query filtering to exclude INACTIVE records by default
 * 
 * @author Dr. Aether Quantum-Singh
 */
@MappedSuperclass
@Getter
@Setter
@Where(clause = "status = 'ACTIVE'")
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EntityStatus status = EntityStatus.ACTIVE;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    /**
     * Performs soft delete by setting status to INACTIVE and recording deletion metadata.
     * 
     * @param deletedBy UUID of the user performing the deletion
     */
    public void softDelete(UUID deletedBy) {
        this.status = EntityStatus.INACTIVE;
        this.deletedAt = Instant.now();
        this.deletedBy = deletedBy;
    }

    /**
     * Reactivates a soft-deleted entity by setting status back to ACTIVE.
     * Clears deletion metadata.
     * 
     * @param reactivatedBy UUID of the user performing the reactivation
     */
    public void reactivate(UUID reactivatedBy) {
        this.status = EntityStatus.ACTIVE;
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedBy = reactivatedBy;
    }

    /**
     * Checks if the entity is soft deleted.
     * 
     * @return true if status is INACTIVE, false otherwise
     */
    public boolean isDeleted() {
        return EntityStatus.INACTIVE.equals(this.status);
    }

    /**
     * Checks if the entity is active.
     * 
     * @return true if status is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return EntityStatus.ACTIVE.equals(this.status);
    }

    /**
     * Sets audit information for entity creation.
     * 
     * @param createdBy UUID of the user creating the entity
     */
    public void setCreationAudit(UUID createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets audit information for entity updates.
     * 
     * @param updatedBy UUID of the user updating the entity
     */
    public void setUpdateAudit(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
}