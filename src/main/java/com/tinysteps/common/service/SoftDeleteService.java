package com.tinysteps.common.service;

import com.tinysteps.common.entity.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base service interface defining soft delete operations contract.
 * All service classes should implement this interface to ensure consistent
 * soft delete behavior across the application.
 * 
 * Design Principles:
 * - Fail-fast validation with clear error messages
 * - Comprehensive authorization checks
 * - Audit trail maintenance
 * - Cascading effect handling
 * 
 * @param <T> Entity type extending BaseEntity
 * @author Dr. Aether Quantum-Singh
 */
public interface SoftDeleteService<T extends BaseEntity> {

    /**
     * Soft delete an entity by ID.
     * 
     * @param id Entity ID to soft delete
     * @param deletedBy UUID of user performing the deletion
     * @return true if deletion was successful, false if entity not found or already deleted
     * @throws IllegalArgumentException if id or deletedBy is null
     * @throws SecurityException if user lacks permission to delete
     */
    boolean softDelete(UUID id, UUID deletedBy);

    /**
     * Bulk soft delete entities by IDs.
     * 
     * @param ids List of entity IDs to soft delete
     * @param deletedBy UUID of user performing the deletion
     * @return number of entities successfully soft deleted
     * @throws IllegalArgumentException if ids is null/empty or deletedBy is null
     * @throws SecurityException if user lacks permission to delete
     */
    int bulkSoftDelete(List<UUID> ids, UUID deletedBy);

    /**
     * Reactivate a soft-deleted entity by ID.
     * 
     * @param id Entity ID to reactivate
     * @param reactivatedBy UUID of user performing the reactivation
     * @return true if reactivation was successful, false if entity not found or not deleted
     * @throws IllegalArgumentException if id or reactivatedBy is null
     * @throws SecurityException if user lacks permission to reactivate
     */
    boolean reactivate(UUID id, UUID reactivatedBy);

    /**
     * Bulk reactivate soft-deleted entities by IDs.
     * 
     * @param ids List of entity IDs to reactivate
     * @param reactivatedBy UUID of user performing the reactivation
     * @return number of entities successfully reactivated
     * @throws IllegalArgumentException if ids is null/empty or reactivatedBy is null
     * @throws SecurityException if user lacks permission to reactivate
     */
    int bulkReactivate(List<UUID> ids, UUID reactivatedBy);

    /**
     * Find active entity by ID.
     * 
     * @param id Entity ID
     * @return Optional containing the entity if found and active, empty otherwise
     */
    Optional<T> findActiveById(UUID id);

    /**
     * Find all active entities.
     * 
     * @return List of all active entities
     */
    List<T> findAllActive();

    /**
     * Find all soft-deleted entities.
     * Requires appropriate permissions.
     * 
     * @param requestedBy UUID of user requesting the data
     * @return List of all soft-deleted entities
     * @throws SecurityException if user lacks permission to view deleted entities
     */
    List<T> findAllDeleted(UUID requestedBy);

    /**
     * Check if entity exists and is active.
     * 
     * @param id Entity ID
     * @return true if entity exists and is active, false otherwise
     */
    boolean existsAndActive(UUID id);

    /**
     * Check if entity is soft deleted.
     * 
     * @param id Entity ID
     * @return true if entity exists and is soft deleted, false otherwise
     */
    boolean isDeleted(UUID id);

    /**
     * Get count of active entities.
     * 
     * @return number of active entities
     */
    long countActive();

    /**
     * Get count of soft-deleted entities.
     * 
     * @param requestedBy UUID of user requesting the count
     * @return number of soft-deleted entities
     * @throws SecurityException if user lacks permission to view deleted entity count
     */
    long countDeleted(UUID requestedBy);

    /**
     * Validate if user has permission to perform soft delete operation.
     * 
     * @param entityId Entity ID
     * @param userId User ID
     * @return true if user has permission, false otherwise
     */
    boolean canSoftDelete(UUID entityId, UUID userId);

    /**
     * Validate if user has permission to perform reactivation operation.
     * 
     * @param entityId Entity ID
     * @param userId User ID
     * @return true if user has permission, false otherwise
     */
    boolean canReactivate(UUID entityId, UUID userId);
}