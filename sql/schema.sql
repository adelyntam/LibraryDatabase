CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

# Replace your_username and your_password with the desired fields for admin
DROP USER IF EXISTS 'your_username'@'%';
CREATE USER 'your_username'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON library_db.* TO 'your_username'@'%';
FLUSH PRIVILEGES;

CREATE TABLE Authors (
author_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
nationality VARCHAR(50),
CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE Members (
member_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
membership_date DATE NOT NULL DEFAULT (CURRENT_DATE),
CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE Books (
book_id INT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(255) NOT NULL,
author_id INT NOT NULL,
genre VARCHAR(50) DEFAULT('General'),
publish_year INT NOT NULL DEFAULT (YEAR(CURRENT_DATE)),
is_available BOOLEAN DEFAULT TRUE,
CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES Authors(author_id),
CONSTRAINT unique_title_author UNIQUE (title, author_id)
);

CREATE INDEX idx_books_availability ON Books(is_available);

CREATE TABLE BorrowRecords (
record_id INT AUTO_INCREMENT PRIMARY KEY,
book_id INT NOT NULL,
member_id INT NOT NULL,
borrow_date DATE NOT NULL,
return_date DATE,
status ENUM('borrowed', 'returned') NOT NULL DEFAULT 'borrowed',
CONSTRAINT fk_borrowed_book FOREIGN KEY (book_id) REFERENCES Books(book_id),
CONSTRAINT fk_borrowing_member FOREIGN KEY (member_id) REFERENCES Members(member_id),
CONSTRAINT check_return_date CHECK (return_date IS NULL OR return_date >= borrow_date)
);

CREATE INDEX idx_borrow_status ON BorrowRecords(status);

CREATE TABLE OrderRequests (
request_id INT AUTO_INCREMENT PRIMARY KEY,
member_id INT NOT NULL,
book_title VARCHAR(255) NOT NULL,
author_name VARCHAR(100) NOT NULL,
request_date DATE NOT NULL DEFAULT (CURRENT_DATE),
status ENUM('pending', 'fulfilled') NOT NULL DEFAULT 'pending',
CONSTRAINT fk_requesting_member FOREIGN KEY (member_id) REFERENCES Members(member_id)
);

CREATE INDEX idx_request_status ON OrderRequests(status);