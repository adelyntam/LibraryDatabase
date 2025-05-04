<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>

<div class="container mt-4">
  <h1>Manage Borrow Records</h1>
  <!-- Search form -->
  <form
    class="row g-3 align-items-center mb-3"
    action="${pageContext.request.contextPath}/manageBorrowRecords"
    method="get"
  >
    <div class="col-auto">
      <label for="searchTerm" class="visually-hidden">Search</label>
      <input
        type="text"
        id="searchTerm"
        name="searchTerm"
        class="form-control"
        placeholder="Book title or Borrower name"
        value="${fn:escapeXml(param.searchTerm)}"
      />
    </div>
    <div class="col-auto">
      <button type="submit" class="btn btn-success">Search</button>
    </div>
  </form>

  <!-- Records table -->
  <table class="table table-striped">
    <thead class="table-light">
      <tr>
        <th>RecordID</th>
        <th>Book Title</th>
        <th>Borrower</th>
        <th>Borrow Date</th>
        <th>Return Date</th>
        <th>Status</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="rec" items="${recordList}">
        <tr>
          <td>${rec.recordId}</td>
          <td>${fn:escapeXml(rec.bookTitle)}</td>
          <td>${fn:escapeXml(rec.borrowerName)}</td>
          <td>
            <fmt:formatDate value="${rec.borrowDate}" pattern="yyyy-MM-dd"/>
          </td>
          <td>
            <c:choose>
              <c:when test="${not empty rec.returnDate}">
                <fmt:formatDate value="${rec.returnDate}" pattern="yyyy-MM-dd"/>
              </c:when>
              <c:otherwise>—</c:otherwise>
            </c:choose>
          </td>
          <td>${rec.status}</td>
        </tr>
      </c:forEach>

      <c:if test="${empty recordList}">
        <tr>
          <td colspan="6" class="text-center text-muted">
            No borrow records found.
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
