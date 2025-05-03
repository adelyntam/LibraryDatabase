<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>
<div class="container mt-4">
<h1>Request a New Book</h1>
    <form action="submitBookRequest" method="post" class="mt-4">
        <div class="mb-3">
            <label for="bookTitle" class="form-label">Book Title</label>
            <input type="text" class="form-control" id="bookTitle" name="bookTitle" required>
        </div>

        <div class="mb-3">
            <label for="authorName" class="form-label">Author Name</label>
            <input type="text" class="form-control" id="authorName" name="authorName" required>
        </div>

        <button type="submit" class="btn btn-success">Submit Request</button>
    </form>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
