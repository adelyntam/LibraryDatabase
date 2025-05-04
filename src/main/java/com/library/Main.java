package com.library;

import com.library.dao.*;
import com.library.model.*;
import com.library.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    private static Connection conn;
    private static int testMemberId;
    private static int testBookId;
    private static int testAuthorId;
    private static int testRequestId;
    private static int testBorrowRecordId;

    public static void main(String[] args) {
        try {
            // Initialize database connection
            conn = DBUtil.getConnection();
            // Turn off so that we can rollback
            conn.setAutoCommit(false);

            testMemberOperations();
            testAuthorOperations();
            testBookOperations();
            testBorrowOperations();
            testOrderRequestOperations();
            testViewOperations();

            System.out.println("\nAll tests completed successfully.");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void testMemberOperations() throws SQLException {
        System.out.println("\nTesting Member Operations");
        MembersDAO membersDAO = new MembersDAO();

        // Create test member
        Member newMember = new Member(0, "TEST MEMBER", "test@example.com", LocalDate.now());
        testMemberId = membersDAO.addMember(conn, newMember);
        System.out.println("Created test member with ID: " + testMemberId);

        // Retrieve member
        Member retrieved = membersDAO.getMemberById(conn, testMemberId);
        System.out.println("Retrieved member: " + retrieved.getName());

        // Update member
        retrieved.setName("UPDATED TEST MEMBER");
        retrieved.setEmail("updated@example.com");
        membersDAO.updateMember(conn, retrieved);
        System.out.println("Updated member information: " + retrieved.getName() + ", " + retrieved.getEmail());

        // Search member
        List<Member> foundMember = membersDAO.searchByName(conn, "TEST");
        System.out.println("Found " + foundMember.size() + " members with TEST in name");

        // Get all members
        List<Member> allMembers = membersDAO.getAllMembers(conn);
        System.out.println("Total members: " + allMembers.size());
    }

    private static void testAuthorOperations() throws SQLException {
        System.out.println("\nTesting Author Operations");
        AuthorsDAO authorsDAO = new AuthorsDAO();

        // Create test author
        Author author = new Author(0, "TEST AUTHOR", "TEST NATIONALITY");
        testAuthorId = authorsDAO.addAuthor(conn, author);
        System.out.println("Created test author with ID: " + testAuthorId);

        // Retrieve author
        Author retrieved = authorsDAO.getAuthorById(conn, testAuthorId);
        System.out.println("Retrieved author: " + retrieved.getName());

        // Update author
        retrieved.setName("UPDATED TEST AUTHOR");
        retrieved.setNationality("UPDATED NATIONALITY");
        authorsDAO.updateAuthor(conn, retrieved);
        System.out.println("Updated author information: " + retrieved.getName() + ", " + retrieved.getNationality());

        // Search author
        List<Author> foundAuthor = authorsDAO.searchByName(conn, "TEST");
        System.out.println("Found " + foundAuthor.size() + " authors with TEST in name");

        // Get all authors
        List<Author> allAuthors = authorsDAO.getAllAuthors(conn);
        System.out.println("Total authors: " + allAuthors.size());
    }

    private static void testBookOperations() throws SQLException {
        System.out.println("\nTesting Book Operations");
        BooksDAO booksDAO = new BooksDAO();

        // Create test book
        Book newBook = new Book(0, "TEST BOOK", testAuthorId, "TEST GENRE", 2023, true);
        testBookId = booksDAO.addBook(conn, newBook);
        System.out.println("Created test book with ID: " + testBookId);

        // Retrieve book
        Book retrieved = booksDAO.getBookById(conn, testBookId);
        System.out.println("Retrieved book: " + retrieved.getTitle());

        // Update book
        retrieved.setTitle("UPDATED TEST BOOK");
        retrieved.setGenre("UPDATED GENRE");
        retrieved.setPublishYear(2024);
        booksDAO.updateBook(conn, retrieved);
        System.out.println("Updated book information: " + retrieved.getTitle() + ", " + retrieved.getGenre());

        // Search book
        List<Book> foundBook = booksDAO.searchByTitle(conn, "TEST");
        System.out.println("Found " + foundBook.size() + " books with TEST in title");

        // Get all books
        List<Book> allBooks = booksDAO.getAllBooks(conn);
        System.out.println("Total books: " + allBooks.size());

        // Check availability
        boolean isAvailable = booksDAO.getBookById(conn, testBookId).isAvailable();
        System.out.println("Book availability: " + isAvailable);
    }

    private static void testBorrowOperations() throws SQLException {
        System.out.println("\nTesting Borrow Operations");
        BorrowRecordsDAO borrowRecordsDAO = new BorrowRecordsDAO();
        BooksDAO booksDAO = new BooksDAO();

        // Borrow test book
        testBorrowRecordId = borrowRecordsDAO.borrowBook(conn, testBookId, testMemberId);
        System.out.println("Borrowed book ID " + testBookId + " for member " + testMemberId +
                ", record ID: " + testBorrowRecordId);

        // Update book availability
        booksDAO.updateAvailability(conn, testBookId, false);
        System.out.println("Updated book availability to false");

        // Verify borrow record exists
        List<BorrowRecord> activeBorrows = borrowRecordsDAO.getActiveBorrows(conn, testMemberId);
        System.out.println("Active borrows count for member " + testMemberId + ": " + activeBorrows.size());

        // Verify book is now unavailable
        boolean bookAvailable = booksDAO.getBookById(conn, testBookId).isAvailable();
        System.out.println("Book should be unavailable: " + !bookAvailable);

        // Return book
        borrowRecordsDAO.returnBook(conn, testBorrowRecordId);
        booksDAO.updateAvailability(conn, testBookId, true);
        System.out.println("Returned book with record ID: " + testBorrowRecordId);

        // Verify book is available again
        bookAvailable = booksDAO.getBookById(conn, testBookId).isAvailable();
        System.out.println("Book should be available again: " + bookAvailable);

        // Get borrow history
        List<BorrowRecord> borrowHistory = borrowRecordsDAO.getBorrowHistory(conn, testMemberId);
        System.out.println("Borrow history count: " + borrowHistory.size());
    }

    private static void testOrderRequestOperations() throws SQLException {
        System.out.println("\nTesting Order Request Operations");
        OrderRequestsDAO orderRequestsDAO = new OrderRequestsDAO();
        BooksDAO booksDAO = new BooksDAO();
        AuthorsDAO authorsDAO = new AuthorsDAO();

        // Create request
        OrderRequest request = new OrderRequest(0, testMemberId, "TEST REQUEST BOOK", "TEST REQUEST AUTHOR", LocalDate.now(), "pending");
        testRequestId = orderRequestsDAO.createRequest(conn, request);
        System.out.println("Created order request ID: " + testRequestId);

        // Retrieve request
        OrderRequest retrieved = orderRequestsDAO.getRequestById(conn, testRequestId);
        System.out.println("Retrieved request status: " + retrieved.getStatus());

        // Get pending requests
        List<OrderRequest> pendingRequests = orderRequestsDAO.getPendingRequests(conn);
        System.out.println("Number of pending requests: " + pendingRequests.size());

        // Fulfill request
        orderRequestsDAO.fulfillRequest(conn, testRequestId);
        System.out.println("Fulfilled request ID: " + testRequestId);

        // Verify request is fulfilled
        retrieved = orderRequestsDAO.getRequestById(conn, testRequestId);
        System.out.println("Request status after fulfillment: " + retrieved.getStatus());

        // Verify book was created
        List<Book> books = booksDAO.searchByTitle(conn, "TEST REQUEST BOOK");
        System.out.println("Found " + books.size() + " books matching the request");

        // Verify author was created
        Integer authorId = authorsDAO.findAuthorIdByName(conn, "TEST REQUEST AUTHOR");
        System.out.println("Author ID for requested book: " + authorId);
    }

    private static void testViewOperations() throws SQLException {
        System.out.println("\nTesting View Operations");
        BooksDAO booksDAO = new BooksDAO();
        MembersDAO membersDAO = new MembersDAO();
        AuthorsDAO authorsDAO = new AuthorsDAO();
        BorrowRecordsDAO borrowRecordsDAO = new BorrowRecordsDAO();

        // View all books with author names
        List<Map<String, Object>> availableBooks = booksDAO.getAvailableBooksWithAuthors(conn);
        System.out.println("Available books count: " + availableBooks.size());

        // View all borrow records with details (check if no error)
        List<Map<String, Object>> allBorrowRecords = borrowRecordsDAO.getAllBorrowRecordsWithDetails(conn);
        System.out.println("Total borrow records: " + allBorrowRecords.size());

        // View detailed borrow history (check if no error)
        List<Map<String, Object>> detailedHistory = borrowRecordsDAO.getBorrowHistoryDetailed(conn, testMemberId);
        System.out.println("Detailed borrow history count: " + detailedHistory.size());

        // Search borrow records
        List<Map<String, Object>> searchedRecords = borrowRecordsDAO.searchByBookOrMember(conn, "TEST");
        System.out.println("Found " + searchedRecords.size() + " borrow records matching TEST");
    }
}