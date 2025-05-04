<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
          <div class="container">
              <ul class="nav nav-pills mb-4">
                <li class="nav-item">
                  <a class="nav-link" href="<c:url value='/manageMember'/>">Members</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="<c:url value='/manageBooks'/>">Books</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="<c:url value='/manageAuthors'/>">Authors</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="<c:url value='/manageRequestBook'/>">Requests</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="<c:url value='/manageBorrowRecords'/>">Borrows</a>
                </li>
              </ul>
          </div>
      </nav>
  </div>
</html>

