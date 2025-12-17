-- Se connecter à MySQL: sudo mysql

CREATE DATABASE IF NOT EXISTS studentdb;
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON studentdb.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
EXIT;