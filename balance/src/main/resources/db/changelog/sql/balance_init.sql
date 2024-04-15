-- Create Balances Table
CREATE TABLE IF NOT EXISTS balances
(
    id               SERIAL PRIMARY KEY,
    account_id       BIGINT  NOT NULL,
    currency_id      INTEGER NOT NULL,
    available_amount BIGINT DEFAULT 0,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_currency FOREIGN KEY (currency_id) REFERENCES currencies (id)
);