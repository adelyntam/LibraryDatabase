<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>

<div class="container mt-4">
    <h1>All Books</h1>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>BookID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Genre</th>
                <th>Publish Year</th>
                <th>Status</th>
                <th>Borrow</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="book" items="${bookList}">
                <tr>
                    <td>${book.bookId}</td>
                    <td>${book.title}</td>
                    <td>${book.authorId}</td>
                    <td>${book.genre}</td>
                    <td>${book.publishYear}</td>
                    <td>
                        <c:choose>
                            <c:when test="${book.available}">
                                <a href="borrowBookView.jsp?bookId=${book.bookId}"
                                   style="display:inline-block; width:30px; height:30px; background-color:limegreen; border-radius:3px; text-decoration:none;">
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div style="width:30px; height:30px; background-color:crimson; border-radius:3px;"></div>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="library?action=view&id=${book.bookId}" class="btn btn-primary btn-sm">
                            View
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>


<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>