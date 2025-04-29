package com.library;

import com.library.model.*;
import com.library.service.LibService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    private static final LibService libService = new LibService();
    private static int testMemberId;
    private static int testBookId;
    private static int testAuthorId;
    private static int testRequestId;
    private static int testBorrowRecordId;


    public static void main(String[] args) {
        try {
            testMemberOperations();

            testBookOperations();

            testBorrowOperations();

            testOrderRequestOperations();

            System.out.println("\nAll tests completed successfully.");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private static void testMemberOperations() throws SQLException {
        System.out.println("\nTesting Member Operations");

        // Create member
        Member newMember = new Member(0, "TEST MEMBER", "test@example.com", LocalDate.now());
        testMemberId = libService.addMember(newMember);
        System.out.println("Created test member with ID: " + testMemberId);

        // Verify creation
        Member retrieved = libService.getMemberById(testMemberId);
        System.out.println("Retrieved member: " + retrieved.getName());
    }

    private static void testBookOperations() throws SQLException {
        System.out.println("\nTesting Book Operations");

        // Create test author first
        Author author = new Author(0, "TEST AUTHOR", "TEST NATIONALITY");
        testAuthorId = libService.addAuthor(author);

        // Create test book
        Book newBook = new Book(0, "TEST BOOK", testAuthorId, "TEST GENRE", 2023, true);
        testBookId = libService.addBook(newBook);
        System.out.println("Created test book with ID: " + testBookId);

        // Verify availability
        List<Map<String, Object>> availableBooks = libService.getAvailableBooksWithAuthors();
        boolean bookExists = availableBooks.stream()
                .anyMatch(b -> (int) b.get("bookId") == testBookId);
        System.out.println("Test book is available: " + bookExists);
    }

    private static void testBorrowOperations() throws SQLException {
        System.out.println("\nTesting Borrow Operations");

        // Borrow test book
        libService.borrowBook(testBookId, testMemberId);
        System.out.println("Borrowed book ID " + testBookId + " for member " + testMemberId);

        // Verify borrow record exists
        List<BorrowRecord> activeBorrows = libService.getActiveBorrows(testMemberId);
        if (!activeBorrows.isEmpty()) {
            testBorrowRecordId = activeBorrows.get(0).getRecordId();
            System.out.println("Created borrow record ID: " + testBorrowRecordId);
        }

        // Verify book is now unavailable
        List<Map<String, Object>> availableBooks = libService.getAvailableBooksWithAuthors();
        boolean bookAvailable = availableBooks.stream()
                .anyMatch(b -> (int) b.get("bookId") == testBookId);
        System.out.println("Book should be unavailable: " + !bookAvailable);
    }

    private static void testOrderRequestOperations() throws SQLException {
        System.out.println("\nTesting Order Request Operations");

        // Create request
        OrderRequest request = new OrderRequest(
                0, testMemberId, "TEST REQUEST BOOK",
                "TEST REQUEST AUTHOR", LocalDate.now(), "pending");

        testRequestId = libService.createOrderRequest(request);
        System.out.println("Created order request ID: " + testRequestId);

        // Verify request exists
        OrderRequest retrieved = libService.getRequestById(testRequestId);
        System.out.println("Retrieved request status: " + retrieved.getStatus());
    }

    private static void cleanupTestData() {
        System.out.println("\nCleaning Up Test Data");
        try {
            // Delete in reverse order of creation to respect foreign keys
            if (testBorrowRecordId != 0) {
                libService.returnBook(testBorrowRecordId);
                System.out.println("Returned test borrow record");
            }

            if (testRequestId != 0) {
                libService.deleteRequest(testRequestId);
                System.out.println("Deleted test request");
            }

            if (testBookId != 0) {
                libService.deleteBook(testBookId);
                System.out.println("Deleted test book");
            }

            if (testAuthorId != 0) {
                libService.deleteAuthor(testAuthorId);
                System.out.println("Deleted test author");
            }

            if (testMemberId != 0) {
                libService.deleteMember(testMemberId);
                System.out.println("Deleted test member");
            }
        } catch (Exception e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }
}