<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>

<div class="container mt-4">
  <h1>History</h1>

  <!-- Search form -->
  <form class="row g-3 mb-3"
        action="${pageContext.request.contextPath}/historyView"
        method="get">
    <div class="col-auto">
      <label for="memberId" class="visually-hidden">MemberID</label>
      <input
        type="text"
        class="form-control"
        id="memberId"
        name="memberId"
        placeholder="MemberID"
        value="${fn:escapeXml(param.memberId)}"
      />
    </div>
    <div class="col-auto">
      <button type="submit" class="btn btn-success">Search</button>
    </div>
  </form>

  <!-- History table -->
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Name</th>
        <th>Author</th>
        <th>Genre</th>
        <th>Date Borrowed</th>
        <th>Date Returned</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="rec" items="${historyList}">
        <tr>
          <td>${rec.bookTitle}</td>
          <td>${rec.bookAuthor}</td>
          <td>${rec.bookGenre}</td>
          <td>
            <fmt:formatDate value="${rec.dateBorrowed}" pattern="yyyy-MM-dd"/>
          </td>
          <td>
            <c:choose>
              <c:when test="${not empty rec.dateReturned}">
                <fmt:formatDate value="${rec.dateReturned}" pattern="yyyy-MM-dd"/>
              </c:when>
              <c:otherwise>—</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:forEach>

      <c:if test="${empty historyList}">
        <tr>
          <td colspan="5" class="text-center text-muted">
            No history found.
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
