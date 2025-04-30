<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.dao.BooksDAO, com.library.model.Book, java.sql.*, java.time.LocalDate" %>
<jsp:include page="header.jsp"/>

<%
    String bookIdStr = request.getParameter("bookId");
    Book book = null;

    if (bookIdStr != null) {
        int bookId = Integer.parseInt(bookIdStr);
        Connection conn = (Connection) application.getAttribute("DBConnection");

        try {
            BooksDAO dao = new BooksDAO();
            book = dao.getBookById(conn, bookId);
        } catch (Exception e) {
            out.println("<p>Error retrieving book: " + e.getMessage() + "</p>");
        }
    }
%>

<div class="container mt-4">
    <h2>Borrow Book</h2>

    <c:if test="${book != null}">
        <form action="borrow" method="post">
            <label for="memberId">Member ID</label>
            <input type="text" name="memberId" id="memberId" required>

            <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
            <button type="submit">Confirm Borrow</button>
        </form>

    </c:if>

    <c:if test="${book == null}">
        <p class="text-danger">Book not found or invalid book ID.</p>
    </c:if>
</div>

<jsp:include page="footer.jsp"/>
