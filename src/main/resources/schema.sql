CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              customer_id VARCHAR(255) NOT NULL,
                              amount DECIMAL(10,2) NOT NULL,
                              transaction_date DATE NOT NULL
);