<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>

<div class="container mt-4">
    <h1>All Books</h1>

    <!-- Search form -->
    <form class="row g-3 mb-4"
          action="<c:url value='/bookListView'/>"
          method="get">
        <div class="col-auto">
            <label for="searchTitle" class="visually-hidden">Title</label>
            <input
                type="text"
                id="searchTitle"
                name="searchTitle"
                class="form-control"
                placeholder="Search by Title"
                value="${fn:escapeXml(param.searchTitle)}"
            />
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-success mb-3">Search</button>
        </div>
    </form>

    <!-- Books table -->
    <table class="table table-striped">
        <thead>
            <tr>
                <th>BookID</th>
                <th>Title</th>
                <th>AuthorID</th>
                <th>Genre</th>
                <th>Publish Year</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="book" items="${bookList}">
                <tr>
                    <td>${book.bookId}</td>
                    <td>${fn:escapeXml(book.title)}</td>
                    <td>${book.authorId}</td>
                    <td>${fn:escapeXml(book.genre)}</td>
                    <td>${book.publishYear}</td>
                    <td>
                        <c:choose>
                            <c:when test="${book.available}">
                                <a href="${pageContext.request.contextPath}/borrowBookView?bookId=${book.bookId}"
                                   style="display:inline-block;width:30px;height:30px;background-color:limegreen;border-radius:3px;"></a>
                            </c:when>
                            <c:otherwise>
                                <div style="width:30px;height:30px;background-color:crimson;border-radius:3px;"></div>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty bookList}">
                <tr>
                    <td colspan="7" class="text-center text-muted">
                        No books found.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
