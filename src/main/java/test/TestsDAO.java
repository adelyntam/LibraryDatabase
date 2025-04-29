package test;

import com.library.dao.*;
import com.library.model.*;
import com.library.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderRequestsDAOTest {
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    private OrderRequestsDAO orderRequestsDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        orderRequestsDAO = new OrderRequestsDAO();
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void createRequest_ValidRequest_ReturnsId() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeUpdate()).thenReturn(1);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(5);

            OrderRequest request = new OrderRequest(0, 1, "Valid Book", "Valid Author",
                    LocalDate.now(), "pending");
            int id = orderRequestsDAO.createRequest(request);
            assertTrue(id > 0);
        }
    }

    @Test
    void createRequest_InvalidData_ThrowsException() {
        OrderRequest invalidRequest = new OrderRequest(0, 1, "", "Valid Author",
                LocalDate.now(), "pending");
        assertThrows(IllegalArgumentException.class, () -> orderRequestsDAO.createRequest(invalidRequest));
    }

    @Test
    void fulfillRequest_ValidId_UpdatesStatus() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            orderRequestsDAO.fulfillRequest(1);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void fulfillRequest_InvalidId_ThrowsException() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            assertThrows(SQLException.class, () -> orderRequestsDAO.fulfillRequest(999));
        }
    }

    @Test
    void deleteRequest_ValidId_DeletesSuccessfully() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            orderRequestsDAO.deleteRequest(1);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void getRequestById_ValidId_ReturnsRequest() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            // Set up resultSet mock to return request data

            OrderRequest request = orderRequestsDAO.getRequestById(1);
            assertNotNull(request);
        }
    }

    @Test
    void getPendingRequests_ReturnsPendingOnly() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(mock(Statement.class));
            when(connection.createStatement().executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            // Set up resultSet mock to return pending request data

            List<OrderRequest> requests = orderRequestsDAO.getPendingRequests();
            assertEquals(1, requests.size());
        }
    }
}

class AuthorsDAOTest {
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    private AuthorsDAO authorsDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        authorsDAO = new AuthorsDAO();
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void addAuthor_ValidAuthor_ReturnsId() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeUpdate()).thenReturn(1);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(5);

            Author author = new Author(0, "Valid Author", "Nationality");
            int id = authorsDAO.addAuthor(author);
            assertTrue(id > 0);
        }
    }

    @Test
    void addAuthor_InvalidName_ThrowsException() {
        Author invalidAuthor = new Author(0, "", "Nationality");
        assertThrows(IllegalArgumentException.class, () -> authorsDAO.addAuthor(invalidAuthor));
    }

    @Test
    void getAuthorById_ValidId_ReturnsAuthor() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            // Set up resultSet mock to return author data

            Author author = authorsDAO.getAuthorById(1);
            assertNotNull(author);
        }
    }

    @Test
    void findAuthorIdByName_ValidName_ReturnsId() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(5);

            Integer id = authorsDAO.findAuthorIdByName("Existing Author");
            assertNotNull(id);
        }
    }

    @Test
    void findAuthorIdByName_NonExistent_ReturnsNull() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(authorsDAO.findAuthorIdByName("Non-existent Author"));
        }
    }
}

class BooksDAOTest {
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    private BooksDAO booksDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        booksDAO = new BooksDAO();
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void addBook_ValidBook_ReturnsId() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeUpdate()).thenReturn(1);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(5);

            Book book = new Book(0, "Valid Book", 1, "Genre", 2023, true);
            int id = booksDAO.addBook(book);
            assertTrue(id > 0);
        }
    }

    @Test
    void updateAvailability_ValidInput_UpdatesSuccessfully() throws SQLException {
        booksDAO.updateAvailability(connection, 1, false);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void getBookById_ValidId_ReturnsBook() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            // Set up resultSet mock to return book data

            Book book = booksDAO.getBookById(1);
            assertNotNull(book);
        }
    }

    @Test
    void getAvailableBooks_ReturnsAvailableOnly() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(mock(Statement.class));
            when(connection.createStatement().executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            // Set up resultSet mock to return available book data

            List<Book> books = booksDAO.getAvailableBooks();
            assertEquals(1, books.size());
        }
    }
}

class BorrowRecordsDAOTest {
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    private BorrowRecordsDAO borrowRecordsDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        borrowRecordsDAO = new BorrowRecordsDAO();
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void borrowBook_ValidInput_ReturnsId() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        int id = borrowRecordsDAO.borrowBook(connection, 1, 1);
        assertTrue(id > 0);
    }

    @Test
    void returnBook_ValidId_UpdatesStatus() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            borrowRecordsDAO.returnBook(1);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void getBorrowRecordById_ValidId_ReturnsRecord() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            // Set up resultSet mock to return record data

            BorrowRecord record = borrowRecordsDAO.getBorrowRecordById(1);
            assertNotNull(record);
        }
    }

    @Test
    void getActiveBorrows_ValidMemberId_ReturnsRecords() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            // Set up resultSet mock to return active borrow data

            List<BorrowRecord> records = borrowRecordsDAO.getActiveBorrows(1);
            assertEquals(1, records.size());
        }
    }
}

class MembersDAOTest {
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    private MembersDAO membersDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        membersDAO = new MembersDAO();
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void addMember_ValidMember_ReturnsId() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeUpdate()).thenReturn(1);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(5);

            Member member = new Member(0, "Valid Member", "valid@test.com", LocalDate.now());
            int id = membersDAO.addMember(member);
            assertTrue(id > 0);
        }
    }

    @Test
    void deleteMember_ValidId_DeletesSuccessfully() throws SQLException {
        membersDAO.deleteMember(connection, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void getMemberById_ValidId_ReturnsMember() throws SQLException {
        try (MockedStatic<DBUtil> dbUtil = mockStatic(DBUtil.class)) {
            dbUtil.when(DBUtil::getConnection).thenReturn(connection);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            // Set up resultSet mock to return member data

            Member member = membersDAO.getMemberById(1);
            assertNotNull(member);
        }
    }
}

