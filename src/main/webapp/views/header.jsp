<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library System</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
    >
    <style>
        body { padding-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="text-center">Online Library</h1>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/views/bookListView.jsp">
                      View Books
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/views/requestBookView_Mem.jsp">
                      Request Book
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/views/returnBookView.jsp">
                      Return Book
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                        href="${pageContext.request.contextPath}/views/historyView.jsp">
                       History
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                        href="${pageContext.request.contextPath}/views/manageMemberView.jsp">
                       Manage
                    </a>
                </li>
            </ul>
        </div>
    </nav>
</div>
