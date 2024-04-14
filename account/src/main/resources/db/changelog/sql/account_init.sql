-- Create Accounts Table
CREATE TABLE IF NOT EXISTS accounts
(
    id          SERIAL PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    country     VARCHAR(100) NOT NULL
);