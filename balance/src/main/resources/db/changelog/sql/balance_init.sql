-- Create Balances Table
CREATE TABLE IF NOT EXISTS balances
(
    id               SERIAL PRIMARY KEY,
    account_id       INT        NOT NULL,
    currency         VARCHAR(3) NOT NULL,
    available_amount BIGINT DEFAULT 0.00,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);