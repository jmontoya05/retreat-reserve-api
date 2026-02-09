-- V11__create_favorites_table.sql
-- Description: Creates the favorites table for user's saved cabins
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE favorites (
    id BINARY(16) PRIMARY KEY,

    -- Foreign keys (UUIDs)
    user_id BINARY(16) NOT NULL,
    cabin_id BINARY(16) NOT NULL,

    -- Audit fields
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorites_cabin FOREIGN KEY (cabin_id)
        REFERENCES cabins(id) ON DELETE CASCADE,

    -- Unique constraint: prevent duplicate favorites
    CONSTRAINT uk_user_cabin UNIQUE (user_id, cabin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_favorites_user ON favorites(user_id, added_at DESC);
CREATE INDEX idx_favorites_cabin ON favorites(cabin_id);

-- Comments for documentation
ALTER TABLE favorites COMMENT = 'Stores user favorite/saved cabins for quick access. Uses UUID (BINARY(16)) for id.';