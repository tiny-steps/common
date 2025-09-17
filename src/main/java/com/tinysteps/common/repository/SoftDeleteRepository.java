package com.tinysteps.common.repository;

import com.tinysteps.common.entity.BaseEntity;
import com.tinysteps.common.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base repository interface providing soft delete functionality.
 * All entity repositories should extend this interface to ensure consistent
 * soft delete behavior across the application.
 * 
 * Key Features:
 * - Automatic filtering of INACTIVE records in standard queries
 * - Explicit methods to query deleted records when needed
 * - Bulk operations for soft delete and reactivation
 * - Audit trail support
 * 
 * @param <T> Entity type extending BaseEntity
 * @author Dr. Aether Quantum-Singh
 */
@NoRepositoryBean
public interface SoftDeleteRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {

    /**
     * Find all active entities (status = ACTIVE).
     * This method explicitly filters for active records.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.status = 'ACTIVE'")
    List<T> findAllActive();

    /**
     * Find all inactive (soft-deleted) entities.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.status = 'INACTIVE'")
    List<T> findAllDeleted();

    /**
     * Find all entities regardless of status.
     * Use with caution - bypasses soft delete filtering.
     */
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAllIncludingDeleted();

    /**
     * Find active entity by ID.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.status = 'ACTIVE'")
    Optional<T> findActiveById(@Param("id") UUID id);

    /**
     * Find entity by ID regardless of status.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id")
    Optional<T> findByIdIncludingDeleted(@Param("id") UUID id);

    /**
     * Check if an active entity exists by ID.
     */
    @Query("SELECT COUNT(e) > 0 FROM #{#entityName} e WHERE e.id = :id AND e.status = 'ACTIVE'")
    boolean existsActiveById(@Param("id") UUID id);

    /**
     * Soft delete entity by ID.
     * Updates status to INACTIVE and sets deletion metadata.
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.status = 'INACTIVE', e.deletedAt = :deletedAt, e.deletedBy = :deletedBy WHERE e.id = :id AND e.status = 'ACTIVE'")
    int softDeleteById(@Param("id") UUID id, @Param("deletedAt") Instant deletedAt, @Param("deletedBy") UUID deletedBy);

    /**
     * Reactivate soft-deleted entity by ID.
     * Updates status to ACTIVE and clears deletion metadata.
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.status = 'ACTIVE', e.deletedAt = NULL, e.deletedBy = NULL, e.updatedAt = :updatedAt, e.updatedBy = :updatedBy WHERE e.id = :id AND e.status = 'INACTIVE'")
    int reactivateById(@Param("id") UUID id, @Param("updatedAt") Instant updatedAt, @Param("updatedBy") UUID updatedBy);

    /**
     * Bulk soft delete entities by IDs.
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.status = 'INACTIVE', e.deletedAt = :deletedAt, e.deletedBy = :deletedBy WHERE e.id IN :ids AND e.status = 'ACTIVE'")
    int softDeleteByIds(@Param("ids") List<UUID> ids, @Param("deletedAt") Instant deletedAt, @Param("deletedBy") UUID deletedBy);

    /**
     * Bulk reactivate entities by IDs.
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.status = 'ACTIVE', e.deletedAt = NULL, e.deletedBy = NULL, e.updatedAt = :updatedAt, e.updatedBy = :updatedBy WHERE e.id IN :ids AND e.status = 'INACTIVE'")
    int reactivateByIds(@Param("ids") List<UUID> ids, @Param("updatedAt") Instant updatedAt, @Param("updatedBy") UUID updatedBy);

    /**
     * Count active entities.
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.status = 'ACTIVE'")
    long countActive();

    /**
     * Count inactive (soft-deleted) entities.
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.status = 'INACTIVE'")
    long countDeleted();

    /**
     * Find entities deleted by a specific user.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedBy = :deletedBy AND e.status = 'INACTIVE'")
    List<T> findDeletedByUser(@Param("deletedBy") UUID deletedBy);

    /**
     * Find entities deleted within a date range.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt BETWEEN :startDate AND :endDate AND e.status = 'INACTIVE'")
    List<T> findDeletedBetweenDates(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}