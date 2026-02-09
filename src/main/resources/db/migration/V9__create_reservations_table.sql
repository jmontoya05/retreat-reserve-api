-- V9__create_reservations_table.sql
-- Description: Creates the reservations table for cabin bookings
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE reservations (
    id BINARY(16) PRIMARY KEY,

    -- Foreign keys (UUIDs)
    user_id BINARY(16) NOT NULL,
    cabin_id BINARY(16) NOT NULL,

    -- Date range (embedded DateRange value object)
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,

    -- Guest details (embedded GuestDetails value object)
    number_of_guests INT NOT NULL,
    guest_name VARCHAR(100),
    guest_phone VARCHAR(20),

    -- Pricing
    total_price DECIMAL(10, 2) NOT NULL,

    -- Status
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',

    -- Additional information
    special_requests TEXT,

    -- Soft delete
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_reservations_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservations_cabin FOREIGN KEY (cabin_id)
        REFERENCES cabins(id) ON DELETE RESTRICT,

    -- Check constraints
    CONSTRAINT chk_number_of_guests CHECK (number_of_guests > 0),
    CONSTRAINT chk_total_price CHECK (total_price > 0),
    CONSTRAINT chk_dates CHECK (check_out_date > check_in_date),
    CONSTRAINT chk_reservation_status CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance (CRITICAL for availability queries)
CREATE INDEX idx_reservations_user ON reservations(user_id);
CREATE INDEX idx_reservations_cabin ON reservations(cabin_id);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_reservations_active ON reservations(active);
CREATE INDEX idx_reservations_created_at ON reservations(created_at DESC);

-- CRITICAL: Composite index for overlap detection queries
CREATE INDEX idx_reservations_cabin_dates ON reservations(cabin_id, check_in_date, check_out_date);

-- Index for finding active reservations by cabin and status
CREATE INDEX idx_reservations_cabin_status ON reservations(cabin_id, status, active);

-- Index for user's reservation history
CREATE INDEX idx_reservations_user_status ON reservations(user_id, status, created_at DESC);

-- Comments for documentation
ALTER TABLE reservations COMMENT = 'Stores cabin reservation/booking information. Uses UUID (BINARY(16)) for id.';