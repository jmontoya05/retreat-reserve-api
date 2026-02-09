-- V1__create_users_table.sql
-- Description: Creates the users table for authentication and user management
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,

    -- Name fields (embedded FullName value object)
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    -- Email field (embedded Email value object)
    email VARCHAR(255) NOT NULL UNIQUE,

    -- Authentication
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,

    -- Role and permissions
    role VARCHAR(20) NOT NULL DEFAULT 'USER',

    -- Email verification
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token VARCHAR(255) UNIQUE,
    verification_token_expiry TIMESTAMP NULL,

    -- Soft delete
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_verification_token ON users(verification_token);
CREATE INDEX idx_users_active ON users(active);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Comments for documentation
ALTER TABLE users COMMENT = 'Stores user accounts for authentication and authorization. Uses UUID (BINARY(16)) for id.';