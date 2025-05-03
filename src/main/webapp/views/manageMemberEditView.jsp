<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>

<div class="container mt-4">
  <!-- Title -->
  <h1>
    <c:choose>
      <c:when test="${not empty member.memberId}">
        Edit Member
      </c:when>
      <c:otherwise>
        Add New Member
      </c:otherwise>
    </c:choose>
  </h1>

  <form
    method="post"
    action="${pageContext.request.contextPath}/manageMemberEdit"
    class="mt-4"
  >
    <!-- if editing, include the ID -->
    <c:if test="${not empty member.memberId}">
      <input
        type="hidden"
        name="memberId"
        value="${member.memberId}"
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
        value="${fn:escapeXml(member.name)}"
      />
    </div>

    <div class="mb-3">
      <label for="email" class="form-label">Email</label>
      <input
        type="email"
        id="email"
        name="email"
        class="form-control"
        required
        value="${fn:escapeXml(member.email)}"
      />
    </div>

    <div class="mb-3">
      <label for="membershipDate" class="form-label">Membership Date</label>
      <input
        type="date"
        id="membershipDate"
        name="membershipDate"
        class="form-control"
        value="${member.membershipDate}"
      />
    </div>

    <button type="submit" class="btn btn-primary">
      <c:choose>
        <c:when test="${not empty member.memberId}">Update</c:when>
        <c:otherwise>Create</c:otherwise>
      </c:choose>
    </button>
    <a
      href="${pageContext.request.contextPath}/manageMember"
      class="btn btn-secondary ms-2"
    >Cancel</a>

    <!-- only show Delete when editing -->
    <c:if test="${not empty member.memberId}">
      <form
        action="${pageContext.request.contextPath}/manageMemberDelete"
        method="post"
        style="display:inline;"
        onsubmit="return confirm('Are you sure you want to delete this member?');"
      >
        <input
          type="hidden"
          name="memberId"
          value="${member.memberId}"
        />
        <button type="submit" class="btn btn-danger float-end">
          Delete
        </button>
      </form>
    </c:if>

  </form>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
