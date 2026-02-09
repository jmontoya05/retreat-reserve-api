-- V5__create_cabin_images_table.sql
-- Description: Creates the cabin_images table for storing cabin photos
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE cabin_images (
    id BINARY(16) PRIMARY KEY,

    -- Foreign key to cabins (UUID)
    cabin_id BINARY(16) NOT NULL,

    -- Image details
    image_url VARCHAR(500) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,

    -- Audit fields
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_cabin_images_cabin FOREIGN KEY (cabin_id)
        REFERENCES cabins(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_cabin_images_cabin ON cabin_images(cabin_id);
CREATE INDEX idx_cabin_images_primary ON cabin_images(cabin_id, is_primary);
CREATE INDEX idx_cabin_images_order ON cabin_images(cabin_id, display_order);

-- Comments for documentation
ALTER TABLE cabin_images COMMENT = 'Stores images associated with cabin listings. Uses UUID (BINARY(16)) for id.';