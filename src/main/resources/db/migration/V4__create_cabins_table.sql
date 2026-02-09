-- V4__create_cabins_table.sql
-- Description: Creates the cabins table for rental properties
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE cabins (
    id BINARY(16) PRIMARY KEY,

    -- Basic cabin information
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,

    -- Foreign key to categories (UUID)
    category_id BINARY(16) NOT NULL,

    -- Location fields (embedded Location value object)
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    address VARCHAR(500),
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),

    -- Capacity fields (embedded Capacity value object)
    max_guests INT NOT NULL,

    -- Cabin specifications
    number_of_bedrooms INT NOT NULL,
    number_of_bathrooms INT NOT NULL,

    -- Pricing
    price_per_night DECIMAL(10, 2) NOT NULL,

    -- Rating summary (denormalized for performance)
    average_rating DECIMAL(3, 2) NOT NULL DEFAULT 0.00,
    total_reviews INT NOT NULL DEFAULT 0,

    -- Status
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',

    -- Soft delete
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_cabins_category FOREIGN KEY (category_id)
        REFERENCES categories(id) ON DELETE RESTRICT,

    -- Check constraints
    CONSTRAINT chk_max_guests CHECK (max_guests > 0),
    CONSTRAINT chk_bedrooms CHECK (number_of_bedrooms > 0),
    CONSTRAINT chk_bathrooms CHECK (number_of_bathrooms > 0),
    CONSTRAINT chk_price CHECK (price_per_night > 0),
    CONSTRAINT chk_cabins_rating CHECK (average_rating >= 0 AND average_rating <= 5),
    CONSTRAINT chk_total_reviews CHECK (total_reviews >= 0),
    CONSTRAINT chk_status CHECK (status IN ('AVAILABLE', 'DISABLED', 'UNDER_MAINTENANCE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance (critical for search queries)
CREATE INDEX idx_cabins_category ON cabins(category_id);
CREATE INDEX idx_cabins_location ON cabins(city, state, country);
CREATE INDEX idx_cabins_city ON cabins(city);
CREATE INDEX idx_cabins_state ON cabins(state);
CREATE INDEX idx_cabins_active_status ON cabins(active, status);
CREATE INDEX idx_cabins_price ON cabins(price_per_night);
CREATE INDEX idx_cabins_rating ON cabins(average_rating DESC);
CREATE INDEX idx_cabins_max_guests ON cabins(max_guests);
CREATE INDEX idx_cabins_created_at ON cabins(created_at DESC);

-- Composite index for availability search
CREATE INDEX idx_cabins_search ON cabins(active, status, city, max_guests);

-- Comments for documentation
ALTER TABLE cabins COMMENT = 'Stores cabin rental properties with location, pricing, and rating information. Uses UUID (BINARY(16)) for id.';