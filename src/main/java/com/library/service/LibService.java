package com.library.service;
// implement library operations here
// business logic
// Borrow/return is for all members
// Fulfill a request is only for admins
// Manage members is only for admins
// *IMPORTANT* note that delete methods are usually for testing only
// make sure to revise if using in logic (cleanup)

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
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Verify book availability
                Book book = booksDao.getBookById(conn, bookId);
                if (book == null || !book.isAvailable()) {
                    throw new IllegalStateException("Book not available for borrowing");
                }

                // Create borrow record
                borrowRecordsDao.borrowBook(conn, bookId, memberId);

                // Update book availability
                booksDao.updateAvailability(conn, bookId, false);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void returnBook(int recordId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Get borrow record
                BorrowRecord record = borrowRecordsDao.getBorrowRecordById(conn, recordId);
                if (record == null) {
                    throw new IllegalArgumentException("Invalid borrow record ID");
                }

                // Update borrow record
                borrowRecordsDao.returnBook(conn, recordId);

                // Update book availability
                booksDao.updateAvailability(conn, record.getBookId(), true);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public int createOrderRequest(OrderRequest request) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return orderRequestsDao.createRequest(conn, request);
        }
    }

    public void fulfillOrderRequest(int requestId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Validate the request exists and isn't already fulfilled
                OrderRequest request = orderRequestsDao.getRequestById(conn, requestId);
                if (request == null) {
                    throw new IllegalArgumentException("Request not found with ID: " + requestId);
                }
                if ("fulfilled".equals(request.getStatus())) {
                    throw new IllegalStateException("Request already fulfilled");
                }

                // Handle author (check existing or create new)
                Integer authorId = authorsDao.findAuthorIdByName(conn, request.getAuthorName());
                if (authorId == null) {
                    Author newAuthor = new Author(0, request.getAuthorName(), "Unknown");
                    authorId = authorsDao.addAuthor(conn, newAuthor);
                    if (authorId == -1) {
                        throw new SQLException("Failed to create new author");
                    }
                }

                // Add the new book, for now default values for genre/year
                Book newBook = new Book(0, request.getBookTitle(), authorId, "General", Year.now().getValue(), true);

                int bookId = booksDao.addBook(conn, newBook);
                if (bookId == -1) {
                    throw new SQLException("Failed to add new book");
                }

                // Mark request as fulfilled
                orderRequestsDao.fulfillRequest(conn, requestId);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public int addMember(Member member) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return membersDao.addMember(conn, member);
        }
    }

    public void deleteMember(int memberId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Check for active borrows
                if (!borrowRecordsDao.getActiveBorrows(conn, memberId).isEmpty()) {
                    throw new IllegalStateException("Cannot delete member with active borrows");
                }

                // Delete member
                membersDao.deleteMember(conn, memberId);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Map<String, Object>> getAvailableBooksWithAuthors() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return booksDao.getAvailableBooksWithAuthors(conn);
        }
    }

    public Member getMemberById(int memberId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return membersDao.getMemberById(conn, memberId);
        }
    }

    public int addAuthor(Author author) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return authorsDao.addAuthor(conn, author);
        }
    }

    public int addBook(Book book) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return booksDao.addBook(conn, book);
        }
    }

    public List<BorrowRecord> getActiveBorrows(int memberId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return borrowRecordsDao.getActiveBorrows(conn, memberId);
        }
    }

    public OrderRequest getRequestById(int requestId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            return orderRequestsDao.getRequestById(conn, requestId);
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // First delete any borrow records for this book
                String deleteBorrowsSql = "DELETE FROM BorrowRecords WHERE book_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteBorrowsSql)) {
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                }

                // Then delete the book
                booksDao.deleteBook(conn, bookId);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void deleteRequest(int requestId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            orderRequestsDao.deleteRequest(conn, requestId);
        }
    }

    public void deleteAuthor(int authorId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            authorsDao.deleteAuthor(conn, authorId);
        }
    }
}
