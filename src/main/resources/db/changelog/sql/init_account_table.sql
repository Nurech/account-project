-- Create Accounts Table
CREATE TABLE IF NOT EXISTS accounts
(
    id          SERIAL PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    country     VARCHAR(100) NOT NULL
);

-- Create Balances Table
CREATE TABLE IF NOT EXISTS balances
(
    id               SERIAL PRIMARY KEY,
    account_id       INT        NOT NULL,
    currency         VARCHAR(3) NOT NULL,
    available_amount BIGINT DEFAULT 0.00,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);

-- Create Transactions Table
CREATE TABLE IF NOT EXISTS transactions
(
    id                    SERIAL PRIMARY KEY,
    account_id            INT        NOT NULL,
    amount                BIGINT     NOT NULL,
    currency              VARCHAR(3) NOT NULL,
    transaction_direction VARCHAR(3) CHECK (transaction_direction IN ('IN', 'OUT')),
    description           TEXT,
    transaction_date      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_transactions FOREIGN KEY (account_id) REFERENCES accounts (id)
);

-- Create Currencies Table
CREATE TABLE IF NOT EXISTS currencies
(
    code       VARCHAR(3) PRIMARY KEY,
    is_allowed BOOLEAN NOT NULL DEFAULT TRUE
);

-- Insert Allowed Currencies
INSERT INTO currencies (code, is_allowed)
VALUES ('EUR', TRUE),
       ('SEK', TRUE),
       ('GBP', TRUE),
       ('USD', TRUE)
ON CONFLICT (code) DO UPDATE SET is_allowed = EXCLUDED.is_allowed;
