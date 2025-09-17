package com.tinysteps.common.exception;

import java.util.UUID;

/**
 * Custom exception for soft delete operations.
 * Provides specific error types and messages for better error handling
 * and user experience.
 * 
 * @author Dr. Aether Quantum-Singh
 */
public class SoftDeleteException extends RuntimeException {

    private final ErrorType errorType;
    private final UUID entityId;

    public SoftDeleteException(ErrorType errorType, UUID entityId, String message) {
        super(message);
        this.errorType = errorType;
        this.entityId = entityId;
    }

    public SoftDeleteException(ErrorType errorType, UUID entityId, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.entityId = entityId;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public UUID getEntityId() {
        return entityId;
    }

    /**
     * Enumeration of soft delete error types.
     */
    public enum ErrorType {
        ENTITY_NOT_FOUND,
        ENTITY_ALREADY_DELETED,
        ENTITY_NOT_DELETED,
        INSUFFICIENT_PERMISSIONS,
        CASCADING_CONSTRAINT_VIOLATION,
        VALIDATION_ERROR,
        CONCURRENT_MODIFICATION
    }

    // Static factory methods for common exceptions

    public static SoftDeleteException entityNotFound(UUID entityId) {
        return new SoftDeleteException(
            ErrorType.ENTITY_NOT_FOUND,
            entityId,
            String.format("Entity with ID %s not found", entityId)
        );
    }

    public static SoftDeleteException entityAlreadyDeleted(UUID entityId) {
        return new SoftDeleteException(
            ErrorType.ENTITY_ALREADY_DELETED,
            entityId,
            String.format("Entity with ID %s is already deleted", entityId)
        );
    }

    public static SoftDeleteException entityNotDeleted(UUID entityId) {
        return new SoftDeleteException(
            ErrorType.ENTITY_NOT_DELETED,
            entityId,
            String.format("Entity with ID %s is not deleted and cannot be reactivated", entityId)
        );
    }

    public static SoftDeleteException insufficientPermissions(UUID entityId, UUID userId) {
        return new SoftDeleteException(
            ErrorType.INSUFFICIENT_PERMISSIONS,
            entityId,
            String.format("User %s lacks permission to perform operation on entity %s", userId, entityId)
        );
    }

    public static SoftDeleteException cascadingConstraintViolation(UUID entityId, String details) {
        return new SoftDeleteException(
            ErrorType.CASCADING_CONSTRAINT_VIOLATION,
            entityId,
            String.format("Cannot delete entity %s due to cascading constraints: %s", entityId, details)
        );
    }

    public static SoftDeleteException validationError(UUID entityId, String validationMessage) {
        return new SoftDeleteException(
            ErrorType.VALIDATION_ERROR,
            entityId,
            String.format("Validation failed for entity %s: %s", entityId, validationMessage)
        );
    }

    public static SoftDeleteException concurrentModification(UUID entityId) {
        return new SoftDeleteException(
            ErrorType.CONCURRENT_MODIFICATION,
            entityId,
            String.format("Entity %s was modified by another process. Please retry the operation", entityId)
        );
    }
}