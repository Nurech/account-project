-- Create Currencies Table
CREATE TABLE IF NOT EXISTS currencies
(
    id         SERIAL PRIMARY KEY,
    code       VARCHAR(3) UNIQUE NOT NULL,
    is_allowed BOOLEAN           NOT NULL DEFAULT TRUE
);

-- Insert Allowed Currencies
INSERT INTO currencies (code, is_allowed)
VALUES ('EUR', TRUE),
       ('SEK', TRUE),
       ('GBP', TRUE),
       ('USD', TRUE)
ON CONFLICT (code) DO NOTHING;