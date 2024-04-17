-- Create Transactions Table
CREATE TABLE IF NOT EXISTS transactions
(
    id                    SERIAL PRIMARY KEY,
    account_id            BIGINT  NOT NULL,
    amount                BIGINT  NOT NULL,
    currency_id           INTEGER NOT NULL,
    transaction_direction VARCHAR(3) CHECK (transaction_direction IN ('IN', 'OUT')),
    description           TEXT,
    transaction_date      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_transactions FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_currency_transactions FOREIGN KEY (currency_id) REFERENCES currencies (id)
);