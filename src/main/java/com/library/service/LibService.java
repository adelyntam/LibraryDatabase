package com.library.service;
// implement library operations here
// business logic
// Borrow/return is for all members
// Fulfill a request is only for admins
// Manage members is only for admins

import com.library.dao.*;
import com.library.model.*;
import com.library.util.DBUtil;
import java.sql.*;
import java.time.Year;
import java.util.List;
import java.util.Map;

public class LibService {
    private final AuthorsDAO authorsDao;
    private final BooksDAO booksDao;
    private final MembersDAO membersDao;
    private final BorrowRecordsDAO borrowRecordsDao;
    private final OrderRequestsDAO orderRequestsDao;

    public LibService() {
        this.authorsDao = new AuthorsDAO();
        this.booksDao = new BooksDAO();
        this.membersDao = new MembersDAO();
        this.borrowRecordsDao = new BorrowRecordsDAO();
        this.orderRequestsDao = new OrderRequestsDAO();
    }

    public void borrowBook(int bookId, int memberId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Verify book availability
            Book book = booksDao.getBookById(bookId);
            if (book == null || !book.isAvailable()) {
                throw new IllegalStateException("Book not available for borrowing");
            }

            // Create borrow record
            borrowRecordsDao.borrowBook(conn, bookId, memberId);

            // Update book availability
            booksDao.updateAvailability(conn, bookId, false);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DBUtil.close(conn);
        }
    }

    public void returnBook(int recordId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Get borrow record
            BorrowRecord record = borrowRecordsDao.getBorrowRecordById(recordId);
            if (record == null) {
                throw new IllegalArgumentException("Invalid borrow record ID");
            }

            // Update borrow record
            borrowRecordsDao.returnBook(recordId);

            // Update book availability
            booksDao.updateAvailability(conn, record.getBookId(), true);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DBUtil.close(conn);
        }
    }

    // Adds new book to library
    public void fulfillOrderRequest(int requestId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Validate the request exists and isn't already fulfilled
            OrderRequest request = orderRequestsDao.getRequestById(requestId);
            if (request == null) {
                throw new IllegalArgumentException("Request not found with ID: " + requestId);
            }
            if ("fulfilled".equals(request.getStatus())) {
                throw new IllegalStateException("Request already fulfilled");
            }

            // Handle author (check existing or create new)
            Integer authorId = authorsDao.findAuthorIdByName(request.getAuthorName());
            if (authorId == null) {
                Author newAuthor = new Author(0, request.getAuthorName(), "Unknown");
                authorId = authorsDao.addAuthor(newAuthor);
                if (authorId == -1) {
                    throw new SQLException("Failed to create new author");
                }
            }

            // Add the new book
            Book newBook = new Book(
                0,
                request.getBookTitle(),
                authorId,
                "General", // For now, default (maybe alter OrderRequest later)
                Year.now().getValue(), // For now, default
                true // Available by default
            );

            int bookId = booksDao.addBook(newBook);
            if (bookId == -1) {
                throw new SQLException("Failed to add new book");
            }

            // Mark request as fulfilled
            orderRequestsDao.fulfillRequest(requestId);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DBUtil.close(conn);
        }
    }

    public int addMember(Member member) throws SQLException {
        // Simple wrapper now, but could add validation/notifications later
        return membersDao.addMember(member);
    }

    public void deleteMember(int memberId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Check for active borrows
            if (!borrowRecordsDao.getActiveBorrows(memberId).isEmpty()) {
                throw new IllegalStateException("Cannot delete member with active borrows");
            }

            // Delete member
            membersDao.deleteMember(conn, memberId);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DBUtil.close(conn);
        }
    }

    public List<Map<String, Object>> getAvailableBooksWithAuthors() throws SQLException {
        // Another simple wrapper
        return booksDao.getAvailableBooksWithAuthors();
    }
}
