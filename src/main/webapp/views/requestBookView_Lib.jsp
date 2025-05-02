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
                    <td>${request.requestedBy}</td>
                    <td>${request.requestDate}</td>
                    <td>
                        <c:choose>
                            <c:when test="${request.status eq 'Pending'}">
                                <span class="badge bg-warning text-dark">Pending</span>
                            </c:when>
                            <c:when test="${request.status eq 'Fulfilled'}">
                                <span class="badge bg-success">Fulfilled</span>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <form action="updateRequestStatus" method="post" class="d-flex align-items-center">
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
