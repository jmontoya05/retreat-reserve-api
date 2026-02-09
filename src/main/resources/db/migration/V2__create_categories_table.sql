-- V2__create_categories_table.sql
-- Description: Creates the categories table for cabin classification
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE categories (
    id BINARY(16) PRIMARY KEY,

    -- Category details
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    image_url VARCHAR(500),

    -- Soft delete
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_categories_active ON categories(active);

-- Comments for documentation
ALTER TABLE categories COMMENT = 'Stores cabin categories (e.g., Standard, Premium, Luxury). Uses UUID (BINARY(16)) for id.';