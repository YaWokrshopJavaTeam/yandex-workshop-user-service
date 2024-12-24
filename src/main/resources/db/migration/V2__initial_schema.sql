ALTER TABLE users ADD registration_type VARCHAR(20);

UPDATE users
SET registration_type = 'MANUAL';