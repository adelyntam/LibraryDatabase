# LibraryDatabase
We are aiming to create an online library management database system that will allow for users to easily track books and the different services that the library offers for them. The system will simplify the process of how to browse a catalog of books, borrow/return books, and request books not currently in the library. This system will greatly improve the efficiency of library services.

Tables & Relationships:
Books - book_id (PK), title, author_id (FK), genre, publish_year (int), is_available (boolean)
Authors - author_id (PK), name, nationality
Members - member_id (PK), name, email, membership_date (date)
BorrowRecords - record_id (PK), book_id (FK), member_id (FK), borrow_date (date), return_date (date), status (borrowed/returned)
OrderRequests - request_id (PK), member_id (FK), book_title, author_name, request_date (date), status (pending/fulfilled)
Authors to Books: one to many relationship, Members to BorrowRecords: one to many relationship, Members to BorrowRecords: one to many relationship, Members to OrderRequests: one to many relationship.

User Roles:
Librarian (Admin): Add/edit/delete books, Manage borrow/return records, View/process book requests, Add/edit/delete member
Member (Regular User): Browse available books, Borrow and return books, Request new books