-- DROP TABLES
DROP TABLE IF EXISTS user_account;
DROP TABLE IF EXISTS user_travel;
DROP TABLE IF EXISTS user_friends;

CREATE TABLE user_account
(
	id INT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100),
    user_password VARCHAR(200),
    email VARCHAR(100),
    phone_number VARCHAR(40),
    user_experience INT DEFAULT 0,
    PRIMARY KEY (id)
)
ENGINE INNODB
CHARSET utf8mb4
COLLATE utf8mb4_swedish_ci
;

CREATE TABLE user_travel
(
	id INT NOT NULL AUTO_INCREMENT,
    user_id INT,
    place VARCHAR(100),
    city VARCHAR(100),
    country VARCHAR(100),
    visit_date TIMESTAMP,
    PRIMARY KEY (id)
)
ENGINE INNODB
CHARSET utf8mb4
COLLATE utf8mb4_swedish_ci
;


CREATE TABLE user_friends
(
	id INT NOT NULL AUTO_INCREMENT,
    user_id INT,
    friend_id INT,
    friends_since TIMESTAMP,
    PRIMARY KEY (id)
)
ENGINE INNODB
CHARSET utf8mb4
COLLATE utf8mb4_swedish_ci
;