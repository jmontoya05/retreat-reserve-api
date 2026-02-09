-- V7__create_policies_table.sql
-- Description: Creates the policies table for cabin usage policies
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE policies (
    id BINARY(16) PRIMARY KEY,

    -- Foreign key to cabins (UUID)
    cabin_id BINARY(16) NOT NULL,

    -- Policy details
    title VARCHAR(100) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_policies_cabin FOREIGN KEY (cabin_id)
        REFERENCES cabins(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_policies_cabin ON policies(cabin_id);
CREATE INDEX idx_policies_order ON policies(cabin_id, display_order);

-- Comments for documentation
ALTER TABLE policies COMMENT = 'Stores policy sections for cabins (e.g., House Rules, Cancellation Policy). Uses UUID (BINARY(16)) for id.';