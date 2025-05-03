<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>

<div class="container mt-4">
    <h2>Borrow Book</h2>

    <c:if test="${not empty book}">
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">Book Details</h5>
                <p class="card-text">
                    <strong>Title:</strong> ${book.title}<br>
                    <strong>ID:</strong> ${book.bookId}<br>
                    <strong>Genre:</strong> ${book.genre}<br>
                    <strong>Publish Year:</strong> ${book.publishYear}
                </p>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/borrowBookView" method="post" class="mb-3">
            <div class="form-group">
                <label for="memberId">Member ID</label>
                <input type="text" class="form-control" name="memberId" id="memberId" required>
            </div>
            <input type="hidden" name="bookId" value="${book.bookId}">
            <button type="submit" class="btn btn-primary mt-3">Confirm Borrow</button>
        </form>
    </c:if>

    <c:if test="${empty book}">
        <div class="alert alert-danger" role="alert">
            Book not found or invalid book ID.
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/bookListView" class="btn btn-secondary">Return to Book List</a>
</div>

<jsp:include page="footer.jsp"/>
