<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>


<div class="container mt-4">
  <h1>Manage Authors</h1>

  <!-- Search + Add New -->
  <form
    class="row g-3 align-items-center mb-3"
    action="${pageContext.request.contextPath}/manageAuthors"
    method="get"
  >
    <div class="col-auto">
      <label for="searchTerm" class="visually-hidden">Search</label>
      <input
        type="text"
        id="searchTerm"
        name="searchTerm"
        class="form-control"
        placeholder="AuthorID or Name"
        value="${fn:escapeXml(param.searchTerm)}"
      />
    </div>
    <div class="col-auto">
      <button type="submit" class="btn btn-success">Search</button>
    </div>
    <div class="col-auto ms-auto">
      <a
        href="${pageContext.request.contextPath}/views/manageAuthorsEditView.jsp"
        class="btn btn-primary"
      >Add New</a>
    </div>
  </form>

  <!-- Results table -->
  <table class="table table-striped">
    <thead class="table-light">
      <tr>
        <th>AuthorID</th>
        <th>Name</th>
        <th>Nationality</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="auth" items="${authorList}">
        <tr>
          <td>
            <a
              href="${pageContext.request.contextPath}/views/manageAuthorsEditView.jsp?authorId=${auth.authorId}"
            >${auth.authorId}</a>
          </td>
          <td>${fn:escapeXml(auth.name)}</td>
          <td>${fn:escapeXml(auth.nationality)}</td>
        </tr>
      </c:forEach>

      <c:if test="${empty authorList}">
        <tr>
          <td colspan="3" class="text-center text-muted">
            No authors found.
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
