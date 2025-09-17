package com.tinysteps.common.entity;

/**
 * Enumeration representing the lifecycle status of entities in the system.
 * Used for soft delete functionality across all services.
 * 
 * Design Philosophy:
 * - ACTIVE: Entity is available for normal operations
 * - INACTIVE: Entity is temporarily deactivated but can be reactivated
 * - DELETED: Entity is soft-deleted but preserved for audit/recovery
 * 
 * Future extensibility allows for additional states like:
 * - PENDING, SUSPENDED, ARCHIVED, etc.
 * 
 * @author Dr. Aether Quantum-Singh
 */
public enum EntityStatus {
    /**
     * Entity is active and available for normal operations.
     * This is the default state for all new entities.
     */
    ACTIVE,

    /**
     * Entity is temporarily deactivated but can be reactivated.
     * Still appears in some queries but not available for operations.
     */
    INACTIVE,

    /**
     * Entity is soft-deleted and should be excluded from normal queries.
     * Data is preserved for audit trails and potential recovery.
     */
    DELETED
}