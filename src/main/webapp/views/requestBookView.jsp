<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>
<div class="container mt-4">
<h1>Request a New Book</h1>
    <form method="post"
             action="<c:url value='/requestBook'/>"
             class="mt-4">
         <div class="mb-3">
            <label for="member_id" class="form-label">Member ID</label>
            <input type="text" class="form-control" id="member_id" name="member_id" required>
        </div>
    
        <div class="mb-3">
            <label for="book_title" class="form-label">Book Title</label>
            <input type="text" class="form-control" id="book_title" name="book_title" required>
        </div>

        <div class="mb-3">
            <label for="author_name" class="form-label">Author Name</label>
            <input type="text" class="form-control" id="author_name" name="author_name" required>
        </div>

        <button type="submit" class="btn btn-success">Submit Request</button>
    </form>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
