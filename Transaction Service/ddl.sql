CREATE SCHEMA tst_db;

CREATE TABLE transactional (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(255),
    currency VARCHAR(255),
    money DECIMAL(18,14),
    createdAt DATETIME,
    date DATETIME
);
