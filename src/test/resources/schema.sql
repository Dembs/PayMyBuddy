DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS connections;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
);

CREATE TABLE account (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE connections (
                             user_id INT,
                             friend_id INT,
                             FOREIGN KEY (user_id) REFERENCES users(id),
                             FOREIGN KEY (friend_id) REFERENCES users(id)
);

CREATE TABLE transaction (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             sender INT,
                             receiver INT,
                             description VARCHAR(100) NOT NULL,
                             amount DOUBLE NOT NULL,
                             fee DOUBLE NOT NULL,
                             date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             type VARCHAR(20) NOT NULL,
                             FOREIGN KEY (sender) REFERENCES users(id),
                             FOREIGN KEY (receiver) REFERENCES users(id)
);