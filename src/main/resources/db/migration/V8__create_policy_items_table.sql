-- V8__create_policy_items_table.sql
-- Description: Creates the policy_items table for individual policy rules
-- Using UUID (BINARY(16)) for primary keys

CREATE TABLE policy_items (
    id BINARY(16) PRIMARY KEY,

    -- Foreign key to policies (UUID)
    policy_id BINARY(16) NOT NULL,

    -- Policy item details
    description VARCHAR(500) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_policy_items_policy FOREIGN KEY (policy_id)
        REFERENCES policies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_policy_items_policy ON policy_items(policy_id);
CREATE INDEX idx_policy_items_order ON policy_items(policy_id, display_order);

-- Comments for documentation
ALTER TABLE policy_items COMMENT = 'Stores individual items/rules within a policy section. Uses UUID (BINARY(16)) for id.';