CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              customer_id VARCHAR(100) NOT NULL,
                              amount DOUBLE PRECISION NOT NULL,
                              date DATE NOT NULL
);