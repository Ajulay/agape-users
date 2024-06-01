CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(255)
);