<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>

<div class="container mt-4">
    <h1>Available Books</h1>
    <table class="table table-striped">
        <thread>
            <tr>
                <th>BookID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Genre</th>
                <th>Publish Year</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thread>
        <body>
            <c:forEach var="book" items="${bookList}">
                <tr>
                    <td>${book.book_id}</td>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.genre}</td>
                    <td>${book.publish_year}</td>
                    <td>
                        <c:choose>
                            <c:when test="${book.is_available}">
                            <!-- Borrow Form -->
                            <form action="borrow" method="post" style="display: inline;">
                                <input type="hidden" name="bookId" value="${book.book_id}" />
                                <button type="submit" class="btn btn-success btn-sm">Borrow</button>
                            </form>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-secondary btn-sm" disabled>Unavailable</button>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="library?action=view&id=${book.book_id}" class="btn btn-primary btn-sm">
                            View
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </body>
    </table>
</div>


<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>