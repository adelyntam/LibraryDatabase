<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>


<div class="container mt-4">
  <!-- Title -->
  <h1>
    <c:choose>
      <c:when test="${not empty book.bookId}">
        Edit Book
      </c:when>
      <c:otherwise>
        Add New Book
      </c:otherwise>
    </c:choose>
  </h1>

  <form
    method="post"
    action="${pageContext.request.contextPath}/manageBooksEdit"
    class="mt-4"
  >
    <!-- if editing, include the ID -->
    <c:if test="${not empty book.bookId}">
      <input
        type="hidden"
        name="bookId"
        value="${book.bookId}"
      />
    </c:if>

    <div class="mb-3">
      <label for="title" class="form-label">Title</label>
      <input
        type="text"
        id="title"
        name="title"
        class="form-control"
        required
        value="${fn:escapeXml(book.title)}"
      />
    </div>

    <div class="mb-3">
      <label for="authorId" class="form-label">Author ID</label>
      <input
        type="number"
        id="authorId"
        name="authorId"
        class="form-control"
        required
        value="${book.authorId}"
      />
    </div>

    <div class="mb-3">
      <label for="genre" class="form-label">Genre</label>
      <input
        type="text"
        id="genre"
        name="genre"
        class="form-control"
        required
        value="${fn:escapeXml(book.genre)}"
      />
    </div>

    <div class="mb-3">
      <label for="publishYear" class="form-label">Publish Year</label>
      <input
        type="number"
        id="publishYear"
        name="publishYear"
        class="form-control"
        required
        value="${book.publishYear}"
      />
    </div>

    <button type="submit" class="btn btn-primary">
      <c:choose>
        <c:when test="${not empty book.bookId}">Update</c:when>
        <c:otherwise>Create</c:otherwise>
      </c:choose>
    </button>
    <a
      href="${pageContext.request.contextPath}/views/manageBooksView.jsp"
      class="btn btn-secondary ms-2"
    >Cancel</a>
  </form>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
