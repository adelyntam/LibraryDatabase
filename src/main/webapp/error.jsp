<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="java.io.*" %>
<html>
<head><title>Error</title></head>
<body>
    <h2>Error</h2>
    <p>${error}</p>
    <% if (exception != null) { %>
        <pre><% exception.printStackTrace(response.getWriter()); %></pre>
    <% } else { %>
        <p>No exception details available</p>
    <% } %>
</body>
</html>