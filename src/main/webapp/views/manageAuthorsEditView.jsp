<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>

<div class="container mt-4">
  <h1>
    <c:choose>
      <c:when test="${not empty author.authorId}">
        Edit Author
      </c:when>
      <c:otherwise>
        Add New Author
      </c:otherwise>
    </c:choose>
  </h1>

  <form
    method="post"
    action="${pageContext.request.contextPath}/manageAuthorsEdit"
    class="mt-4"
  >
    <!-- editing: include the ID -->
    <c:if test="${not empty author.authorId}">
      <input
        type="hidden"
        name="authorId"
        value="${author.authorId}"
      />
    </c:if>

    <div class="mb-3">
      <label for="name" class="form-label">Name</label>
      <input
        type="text"
        id="name"
        name="name"
        class="form-control"
        required
        value="${fn:escapeXml(author.name)}"
      />
    </div>

    <div class="mb-3">
      <label for="nationality" class="form-label">Nationality</label>
      <input
        type="text"
        id="nationality"
        name="nationality"
        class="form-control"
        value="${fn:escapeXml(author.nationality)}"
      />
    </div>

    <button type="submit" class="btn btn-primary">
      <c:choose>
        <c:when test="${not empty author.authorId}">Update</c:when>
        <c:otherwise>Create</c:otherwise>
      </c:choose>
    </button>
    <a
      href="${pageContext.request.contextPath}/views/manageAuthorsView.jsp"
      class="btn btn-secondary ms-2"
    >Cancel</a>
  </form>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
