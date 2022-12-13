-- Create Database
CREATE DATABASE IF NOT EXISTS exploro;

USE exploro;

-- Create user
DROP USER IF EXISTS 'user'@'%';
CREATE USER IF NOT EXISTS 'user'@'%'
    IDENTIFIED WITH mysql_native_password
    BY 'pass'
;

-- Give user all rights to the database
GRANT ALL PRIVILEGES
    ON *.*
    TO 'user'@'%'
;