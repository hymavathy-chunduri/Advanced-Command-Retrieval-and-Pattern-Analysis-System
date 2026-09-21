-- Database Schema for Linux Command Intelligence System
-- Database Name: linux_command_db

DROP TABLE IF EXISTS linux_commands CASCADE;

CREATE TABLE linux_commands (
    id BIGSERIAL PRIMARY KEY,
    command VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(100) NOT NULL,
    short_definition TEXT NOT NULL,
    description TEXT NOT NULL,
    syntax TEXT NOT NULL,
    example TEXT NOT NULL,
    example_explanation TEXT NOT NULL,
    common_options TEXT NOT NULL,
    related_commands TEXT NOT NULL,
    safety_level VARCHAR(30) DEFAULT 'safe',
    distribution VARCHAR(100) DEFAULT 'All Major Linux Distributions',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Performance Indexes for Quick Access
CREATE INDEX idx_linux_commands_command ON linux_commands (command);
CREATE INDEX idx_linux_commands_category ON linux_commands (category);
CREATE INDEX idx_linux_commands_safety_level ON linux_commands (safety_level);

-- Trigger to auto-update timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_linux_commands_updated_at
BEFORE UPDATE ON linux_commands
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
