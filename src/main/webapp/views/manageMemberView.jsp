<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>

<div class="container mt-4">
  <h1>Manage Members</h1>
  <form
    class="row g-3 align-items-center mb-3"
    action="${pageContext.request.contextPath}/views/manageMember"
    method="get"
  >
    <div class="col-auto">
      <label for="searchTerm" class="visually-hidden">Search</label>
      <input
        type="text"
        id="searchTerm"
        name="searchTerm"
        class="form-control"
        placeholder="MemberID or Name"
        value="${fn:escapeXml(param.searchTerm)}"
      />
    </div>
    <div class="col-auto">
      <button type="submit" class="btn btn-success">Search</button>
    </div>
    <div class="col-auto ms-auto">
      <a
        href="${pageContext.request.contextPath}/views/manageMemberEdit.jsp"
        class="btn btn-primary"
      >Add New</a>
    </div>
  </form>

  <!-- always render the table header -->
  <table class="table table-striped">
    <thead class="table-light">
      <tr>
        <th>MemberID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Membership Date</th>
      </tr>
    </thead>
    <tbody>
      <!-- render one row per member -->
      <c:forEach var="mem" items="${memberList}">
        <tr>
          <td>
            <a
              href="${pageContext.request.contextPath}/views/manageMemberEdit.jsp?memberId=${mem.memberId}"
            >${mem.memberId}</a>
          </td>
          <td>${mem.name}</td>
          <td>${mem.email}</td>
          <td>
            <fmt:formatDate
              value="${mem.membershipDate}"
              pattern="yyyy-MM-dd"
            />
          </td>
        </tr>
      </c:forEach>

      <!-- if the list was empty, show a placeholder row -->
      <c:if test="${empty memberList}">
        <tr>
          <td colspan="4" class="text-center text-muted">
            No members found.
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
