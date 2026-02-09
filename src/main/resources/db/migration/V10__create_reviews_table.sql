-- V10__create_reviews_table.sql
-- Description: Creates the reviews table for cabin ratings and comments
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE reviews (
    id BINARY(16) PRIMARY KEY,

    -- Foreign keys (UUIDs)
    user_id BINARY(16) NOT NULL,
    cabin_id BINARY(16) NOT NULL,
    reservation_id BINARY(16) NOT NULL,

    -- Rating (embedded Rating value object)
    rating INT NOT NULL,

    -- Review content
    comment TEXT,

    -- Soft delete
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reviews_cabin FOREIGN KEY (cabin_id)
        REFERENCES cabins(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_reservation FOREIGN KEY (reservation_id)
        REFERENCES reservations(id) ON DELETE RESTRICT,

    -- Unique constraint: one review per reservation
    CONSTRAINT uk_review_reservation UNIQUE (user_id, cabin_id, reservation_id),

    -- Check constraints
    CONSTRAINT chk_reviews_rating CHECK (rating >= 1 AND rating <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_reviews_user ON reviews(user_id);
CREATE INDEX idx_reviews_cabin ON reviews(cabin_id);
CREATE INDEX idx_reviews_reservation ON reviews(reservation_id);
CREATE INDEX idx_reviews_active ON reviews(active);
CREATE INDEX idx_reviews_created_at ON reviews(created_at DESC);

-- Index for fetching cabin reviews (most common query)
CREATE INDEX idx_reviews_cabin_active ON reviews(cabin_id, active, created_at DESC);

-- Index for rating calculations
CREATE INDEX idx_reviews_cabin_rating ON reviews(cabin_id, active, rating);

-- Comments for documentation
ALTER TABLE reviews COMMENT = 'Stores user reviews and ratings for cabins. Uses UUID (BINARY(16)) for id.';