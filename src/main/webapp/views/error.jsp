<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>

<div class="container mt-4">
    <div class="alert alert-danger" role="alert">
        <h4 class="alert-heading">Error</h4>
        <p>${error}</p>
        <hr>
        <p class="mb-0">
            <a href="<c:url value='/bookListView'/>" class="alert-link">Return to Book List</a>
        </p>
    </div>
</div>

<jsp:include page="footer.jsp"/>
