USE library_db;

INSERT INTO Authors (name, nationality) VALUES
('Charles Dickens', 'English'),
('Antoine de Saint-Exupéry', 'French'),
('Paulo Coelho', 'Portuguese'),
('J.K. Rowling', 'English'),
('Agatha Christie', 'English'),
('Cao Xueqin', 'Chinese'),
('J.R.R. Tolkien', 'English'),
('Lewis Carroll', 'English'),
('C.S. Lewis', 'English'),
('H. Rider Haggard', 'English'),
('Dan Brown', 'English'),
('J.D. Salinger', 'English'),
('Robert James Waller', 'English'),
('Gabriel García Márquez', 'Colombian'),
('Vladimir Nabokov', 'Russian')
;

INSERT INTO Members (name, email, membership_date) VALUES
('Ahri', 'ahri@example.com', '2005-06-15'),
('Yasuo', 'yasuo@example.com', '2007-11-22'),
('Lux', 'lux@example.com', '2008-03-10'),
('Ezreal', 'ezreal@example.com', '2010-09-05'),
('Jinx', 'jinx@example.com', '2012-01-18'),
('Darius', 'darius@example.com', '2013-07-30'),
('Miss Fortune', 'missfortune@example.com', '2014-04-12'),
('Thresh', 'thresh@example.com', '2015-08-25'),
('Kai''Sa', 'kaisa@example.com', '2016-12-08'),
('Sett', 'sett@example.com', '2018-02-14'),
('Seraphine', 'seraphine@example.com', '2019-05-20'),
('Viego', 'viego@example.com', '2020-10-31'),
('Samira', 'samira@example.com', '2021-07-04'),
('Zeri', 'zeri@example.com', '2023-01-15'),
('Hwei', 'hwei@example.com', '2025-03-21'),
('K''Sante', 'ksante@example.com', '2025-02-15'),
('Milio', 'milio@example.com', '2025-01-30'),
('Naafiri', 'naafiri@example.com', '2024-09-01')
;

INSERT INTO Books (book_id, title, author_id, genre, publish_year, is_available) VALUES
(1, 'A Tale of Two Cities', 1, 'Historical fiction', 1859, TRUE),
(2, 'The Little Prince', 2, 'Fantasy', 1943, TRUE),
(3, 'The Alchemist', 3, 'Fantasy', 1988, FALSE),
(4, 'Harry Potter and the Philosopher''s Stone', 4, 'Fantasy', 1997, FALSE),
(5, 'And Then There Were None', 5, 'Mystery', 1939, FALSE),
(6, 'Dream of the Red Chamber', 6, 'Family saga', 1791, FALSE),
(7, 'The Hobbit', 7, 'Fantasy', 1937, TRUE),
(8, 'Alice''s Adventures in Wonderland', 8, 'Fantasy', 1865, TRUE),
(9, 'The Lion, the Witch and the Wardrobe', 9, 'Fantasy', 1950, FALSE),
(10, 'She: A History of Adventure', 10, 'Adventure', 1887, TRUE),
(11, 'The Da Vinci Code', 11, 'Mystery thriller', 2003, FALSE),
(12, 'The Catcher in the Rye', 12, 'Coming-of-age', 1951, TRUE),
(13, 'The Bridges of Madison County', 13, 'Romance', 1992, FALSE),
(14, 'One Hundred Years of Solitude', 14, 'Magic realism', 1967, TRUE),
(15, 'Lolita', 15, 'Novel', 1955, FALSE)
;

INSERT INTO BorrowRecords (book_id, member_id, borrow_date, return_date, status) VALUES
(1, 1, '2010-01-10', '2010-01-25', 'returned'),
(2, 2, '2015-02-15', '2015-03-01', 'returned'),
(7, 18, '2025-01-20', '2025-02-28', 'returned'),
(8, 4, '2013-08-30', '2013-09-14', 'returned'),
(10, 6, '2024-10-25', '2024-11-09', 'returned'),
(12, 8, '2016-12-08', '2016-12-23', 'returned'),
(14, 10, '2017-02-19', '2017-03-06', 'returned'),
(3, 3, '2020-03-20', NULL, 'borrowed'),
(4, 15, '2025-01-05', NULL, 'borrowed'),
(5, 16, '2025-02-10', NULL, 'borrowed'),
(6, 17, '2025-03-15', NULL, 'borrowed'),
(9, 5, '2021-09-14', NULL, 'borrowed'),
(11, 7, '2025-03-01', NULL, 'borrowed'),
(13, 9, '2025-02-17', NULL, 'borrowed'),
(15, 11, '2025-01-21', NULL, 'borrowed')
;

INSERT INTO OrderRequests (member_id, book_title, author_name, request_date, status) VALUES
(1, 'The Adventures of Pinocchio', 'Carlo Collodi', '2006-05-15', 'pending'),
(2, 'The Diary of Anne Frank', 'Anne Frank', '2008-12-10', 'pending'),
(3, 'To Kill a Mockingbird', 'Harper Lee', '2009-08-22', 'pending'),
(15, 'War and Peace', 'Leo Tolstoy', '2025-01-18', 'pending'),
(16, 'The Great Gatsby', 'F. Scott Fitzgerald', '2025-02-10', 'pending'),
(17, 'Gone with the Wind', 'Margaret Mitchell', '2025-03-14', 'pending'),
(4, 'The Kite Runner', 'Khaled Hosseini', '2011-04-18', 'pending'),
(5, 'The Hunger Games', 'Suzanne Collins', '2013-11-30', 'pending'),
(6, 'The Girl with the Dragon Tattoo', 'Stieg Larsson', '2014-07-14', 'pending'),
(7, 'Pride and Prejudice', 'Jane Austen', '2015-09-25', 'pending'),
(8, 'Dune', 'Frank Herbert', '2017-03-08', 'pending'),
(9, 'The Secret', 'Rhonda Byrne', '2018-10-12', 'pending'),
(10, 'The Fault in Our Stars', 'John Green', '2019-06-20', 'pending'),
(11, 'The Godfather', 'Mario Puzo', '2020-02-28', 'pending'),
(12, 'Charlie and the Chocolate Factory', 'Roald Dahl', '2021-12-15', 'pending'),
(13, 'The Hobbit', 'J.R.R. Tolkien', '2022-08-05', 'pending'),
(14, 'Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', '2023-04-30', 'pending'),
(18, 'The Da Vinci Code', 'Dan Brown', '2025-03-28', 'pending')
;