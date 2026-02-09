-- V6__create_cabin_features_table.sql
-- Description: Creates the cabin_features join table for many-to-many relationship
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE cabin_features (
    id BINARY(16) PRIMARY KEY,

    -- Foreign keys (UUIDs)
    cabin_id BINARY(16) NOT NULL,
    feature_id BINARY(16) NOT NULL,

    -- Audit fields
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_cabin_features_cabin FOREIGN KEY (cabin_id)
        REFERENCES cabins(id) ON DELETE CASCADE,
    CONSTRAINT fk_cabin_features_feature FOREIGN KEY (feature_id)
        REFERENCES features(id) ON DELETE CASCADE,

    -- Unique constraint to prevent duplicate features on same cabin
    CONSTRAINT uk_cabin_feature UNIQUE (cabin_id, feature_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_cabin_features_cabin ON cabin_features(cabin_id);
CREATE INDEX idx_cabin_features_feature ON cabin_features(feature_id);

-- Comments for documentation
ALTER TABLE cabin_features COMMENT = 'Join table linking cabins to their features/amenities. Uses UUID (BINARY(16)) for id.';