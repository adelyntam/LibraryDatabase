<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <a class="nav-link" href="<c:url value='/bookListView'/>">View Books</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/requestBook'/>">Request Book</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/returnBook'/>">Return Book</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/historyView'/>">History</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/manageLogin'/>">Manage</a>
                </li>
            </ul>
        </div>
    </nav>
</div>
