<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  </head>
  <body>
    <div class="container mt-4">
      <h1>Error</h1>
      <div class="alert alert-danger">
        <c:choose>
          <c:when test="${not empty error}">
            ${error}
          </c:when>
          <c:otherwise>
            An unexpected error occurred.
          </c:otherwise>
        </c:choose>
      </div>
      <a href="${pageContext.request.contextPath}/bookListView" class="btn btn-primary">Back to Book List</a>
    </div>
  </body>
</html>
