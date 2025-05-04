<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.time.LocalDate" %>
<%@ page import="com.library.dao.BorrowRecordsDAO, com.library.dao.BooksDAO, com.library.model.BorrowRecord, com.library.model.Book, com.library.util.DBUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<div class="container mt-4">
    <h1>Return Book</h1>

    <form method="get" action="<c:url value='/views/returnBookView.jsp'/>">
        <div class="mb-3">
            <label for="memberId" class="form-label">Member ID</label>
            <input type="text" name="memberId" id="memberId" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="bookId" class="form-label">Book ID</label>
            <input type="text" name="bookId" id="bookId" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-success">Search</button>
    </form>

    <%
        String bookIdParam = request.getParameter("bookId");
        String memberIdParam = request.getParameter("memberId");
        if (bookIdParam != null && memberIdParam != null) {
            int bookId = Integer.parseInt(bookIdParam);
            int memberId = Integer.parseInt(memberIdParam);

            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                BorrowRecordsDAO borrowDAO = new BorrowRecordsDAO();
                BorrowRecord record = borrowDAO.getActiveBorrow(conn, bookId, memberId);
                if (record != null) {
                    BooksDAO booksDAO = new BooksDAO();
                    Book book = booksDAO.getBookById(conn, bookId);
    %>

    <hr>
    <h5>Borrowed Book Details</h5>
    <p><strong>Book Name:</strong> <%= book.getTitle() %></p>
    <p><strong>Book Author ID:</strong> <%= book.getAuthorId() %></p>
    <p><strong>Borrower ID:</strong> <%= memberId %></p>
    <p><strong>Date Borrowed:</strong> <%= record.getBorrowDate() %></p>
    <p><strong>Status:</strong> <%= record.getStatus() %></p>

    <form method="post" action="<c:url value='/returnBook'/>" class="mt-4">
        <input type="hidden" name="recordId" value="<%= record.getRecordId() %>">
        <input type="hidden" name="bookId" value="<%= bookId %>">
        <button type="submit" class="btn btn-success">Confirm Return</button>
    </form>

    <%
                } else {
    %>
                    <p class="text-danger mt-3">Book not checked out by this member.</p>
    <%
                }
            } catch (Exception e) {
                out.println("<p class='text-danger'>Error: " + e.getMessage() + "</p>");
            } finally {
                DBUtil.close(conn);
            }
        }
    %>
</div>

<jsp:include page="footer.jsp" />
