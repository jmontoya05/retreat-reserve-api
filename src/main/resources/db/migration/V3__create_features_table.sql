-- V3__create_features_table.sql
-- Description: Creates the features table for cabin amenities/characteristics
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE features (
    id BINARY(16) PRIMARY KEY,

    -- Feature details
    name VARCHAR(100) NOT NULL UNIQUE,
    icon_key VARCHAR(500) NOT NULL,
    description VARCHAR(500),

    -- Soft delete
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_features_name ON features(name);
CREATE INDEX idx_features_active ON features(active);

-- Comments for documentation
ALTER TABLE features COMMENT = 'Stores reusable cabin features (WiFi, Pool, Parking, etc.). Uses UUID (BINARY(16)) for id.';