ALTER TABLE users ADD registration_type VARCHAR(20) NOT NULL;

UPDATE users
SET registration_type = 'MANUAL';