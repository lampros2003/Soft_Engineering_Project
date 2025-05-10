-- Create database
CREATE DATABASE IF NOT EXISTS analytics_db;

-- Create user for localhost
CREATE USER IF NOT EXISTS 'analytics_user'@'localhost' IDENTIFIED BY 'analytics_password';
GRANT ALL PRIVILEGES ON analytics_db.* TO 'analytics_user'@'localhost';

-- Create user for remote connections
CREATE USER IF NOT EXISTS 'analytics_user'@'%' IDENTIFIED BY 'analytics_password';
GRANT ALL PRIVILEGES ON analytics_db.* TO 'analytics_user'@'%';

-- Apply changes
FLUSH PRIVILEGES;

-- Use the database
USE analytics_db;
