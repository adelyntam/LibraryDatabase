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

Setup Steps:

1. Download MYSQL (using 9.3)
2. Download IntelliJ IDEA and Tomcat
3. Sync Maven dependencies using pom.xml
4. Configure Tomcat with WAR created by project after building it (images below)
5. Default url should be http://localhost:8080/library/
6. Start MYSQL
7. Change create_schema.sql to desired user and password
8. Change db.properties.template to db.properties and change to desired user and password
9. Run create_schema.sql once by using \. (path to create_schema.sql) with the root user (or user with admin privileges) of your MySQL configuration
10. Populate with sample data by running initialize_data.sql (if desired)
11. Run Project with Tomcat (future runs skip all steps except 6 and 11)

Relevant images -

Syncing Maven dependencies:

![Screenshot 2025-05-04 144222](https://github.com/user-attachments/assets/04226f17-8f04-4fdd-8a32-ca9ee03e23f4)


Building WAR:

![Screenshot 2025-05-04 144334](https://github.com/user-attachments/assets/5ee22428-dceb-4585-b34d-fa74f016e1fd)
![Screenshot 2025-05-04 144347](https://github.com/user-attachments/assets/2192df24-716f-4d33-bb10-8c63caa8f5cb)


Configuring Tomcat:

![image](https://github.com/user-attachments/assets/163a37cd-1055-466d-97b3-a215382877e7)

![image](https://github.com/user-attachments/assets/580d0dfb-7716-4ac2-9927-150cec633235)

![Screenshot 2025-05-04 144408](https://github.com/user-attachments/assets/6ae8a503-8d89-48e7-b013-3a6804c4d7d4)

- Find path to Tomcat

![Screenshot 2025-05-04 144427](https://github.com/user-attachments/assets/ecfb8b29-7864-419e-bf64-22de1b4096cc)

- Deployment settings (make sure to rename the application context to library)
  
![Screenshot 2025-05-04 144534](https://github.com/user-attachments/assets/2fc3125d-ed4e-471a-98b7-a571f9f0b1c2)
![Screenshot 2025-05-04 144544](https://github.com/user-attachments/assets/97ce3c7a-37bd-4c0d-98d9-6318657544b0)
