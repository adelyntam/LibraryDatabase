<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>

<div class="container mt-4">
  <h1>Manage Books</h1>

  <!-- Search + Add New -->
  <form
    class="row g-3 mb-4"
    action="<c:url value='/manageBooks'/>"
    method="get"
  >
    <div class="col-auto">
      <label for="searchTerm" class="visually-hidden">Search</label>
      <input
        type="text"
        id="searchTerm"
        name="searchTerm"
        class="form-control"
        placeholder="Book Title"
        value="${fn:escapeXml(param.searchTerm)}"
      />
    </div>
    <div class="col-auto">
      <button type="submit" class="btn btn-success">Search</button>
    </div>
    <div class="col-auto ms-auto">
      <a
        href="${pageContext.request.contextPath}/views/manageBooksEditView.jsp"
        class="btn btn-primary"
      >Add New</a>
    </div>
  </form>

  <!-- Results table -->
  <table class="table table-striped">
    <thead class="table-light">
      <tr>
        <th>BookID</th>
        <th>Title</th>
        <th>Author</th>
        <th>Genre</th>
        <th>Publish Year</th>
        <th>Available</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="book" items="${bookList}">
        <tr>
          <td>
            <a
              href="${pageContext.request.contextPath}/manageBooksEdit?bookId=${book.bookId}"
            >${book.bookId}</a>
          </td>
          <td>${book.title}</td>
          <td>${book.authorId}</td>
          <td>${book.genre}</td>
          <td>${book.publishYear}</td>
          <td>
            <c:choose>
              <c:when test="${book.available}">Yes</c:when>
              <c:otherwise>No</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:forEach>

      <c:if test="${empty bookList}">
        <tr>
          <td colspan="6" class="text-center text-muted">
            No books found.
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
