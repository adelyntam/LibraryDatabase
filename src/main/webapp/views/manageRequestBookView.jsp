<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>
<jsp:include page="headerLib.jsp"/>

<div class="container mt-4">
    <h1>Manage Book Requests</h1>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Request ID</th>
                <th>Book Title</th>
                <th>Book Author</th>
                <th>Requested By</th>
                <th>Request Date</th>
                <th>Current Status</th>
                <th>Update Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="request" items="${requestList}">
                <tr>
                    <td>${request.requestId}</td>
                    <td>${request.bookTitle}</td>
                    <td>${request.authorName}</td>
                    <td>${request.memberId}</td>
                    <td>${request.requestDate}</td>
                    <td>${request.status}</td>
                    <td>
                        <form action="<c:url value='/manageRequestBook'/>" method="post" class="mt-4">
                            <input type="hidden" name="requestId" value="${request.requestId}" />
                            <select name="newStatus" class="form-select form-select-sm" onchange="this.form.submit()" required>
                                <option value="">Select</option>
                                <option value="Pending" ${request.status eq 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="Fulfilled" ${request.status eq 'Fulfilled' ? 'selected' : ''}>Fulfilled</option>
                            </select>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
